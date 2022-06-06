package com.microservicios.operativo.controllers;

import java.io.IOException;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.entities.ArchivoSFM;
import com.microservicios.operativo.models.entities.DiligenciaSFM;
import com.microservicios.operativo.services.ArchivoSFMService;
import com.microservicios.operativo.services.DiligenciaSFMService;
import com.microservicios.operativo.services.EvaluarSolicitudSFMService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "*" })
@RestController
public class DiligenciaSFMController {

   @Autowired
   private DiligenciaSFMService service;

   @Autowired
   private EvaluarSolicitudSFMService evaluarSolicitudService;

   @Autowired
   private ArchivoSFMService archivoDiligenciaService;

   @DeleteMapping( path = { "/deleteDiligenciaById/{idDiligencia}" } )
   public ResponseEntity<?> deleteDiligenciaById(@PathVariable Long idDiligencia) {
       this.service.deleteById(idDiligencia);
       return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_DELETE_RECORD)
                                       .data(this.evaluarSolicitudService.findAll())
                                       .build());
   }

   @PutMapping(path = { "/saveArchivoDiligencia/{idDiligencia}" })
   public ResponseEntity<?> saveArchivoDiligencia(@PathVariable Long idDiligencia, @RequestPart MultipartFile file) throws IOException {
      DiligenciaSFM diligenciaSFM = this.service
                                             .findById(idDiligencia)
                                             .orElseThrow(DataAccessEmptyWarning::new);

      String fileName      = FilenameUtils.getBaseName(file.getOriginalFilename()),
             fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
      
      ArchivoSFM archivoSFMNew = ArchivoSFM
                                    .of()
                                    .diligencia(diligenciaSFM)
                                    .nombre(fileName)
                                    .tipoArchivo(fileExtension)
                                    .archivo(file.getBytes())
                                    .get();

      this.archivoDiligenciaService.save(archivoSFMNew);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_UPLOAD_FILE)
                                       .data(this.evaluarSolicitudService.findAll())
                                       .build());
   }
   
   @GetMapping( path = { "/downloadArchivoDiligencia" } )
   public ResponseEntity<?> downloadArchivoDiligencia(@RequestParam Long idArchivoDiligencia) {

      ArchivoSFM archivoSFM = this.archivoDiligenciaService
                                       .findById(idArchivoDiligencia)
                                       .orElseThrow(DataAccessEmptyWarning::new);


      Resource file = new ByteArrayResource(archivoSFM.getArchivo());

      String contentDisposition = "attachment; filename=\"%s\"",
             fileName = archivoSFM.getNombre(),
             fileExtension = archivoSFM.getTipoArchivo();
      
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, fileName.concat(".").concat(fileExtension)));

      return ResponseEntity
                  .ok()
                  .headers(headers)
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(file);
   }

   @DeleteMapping( path = { "/deleteArchivoDiligencia" } )
   public ResponseEntity<?> deleteArchivoDiligencia(@RequestParam Long idArchivoDiligencia) {
      this.archivoDiligenciaService.deleteById(idArchivoDiligencia);
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_DELETE_RECORD)
                                       .data(this.evaluarSolicitudService.findAll())
                                       .build());
   }

}