package com.microservicio.rrhh.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.RimHttpHeaders;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rrhh.models.dto.ControlPermisosDto;
import com.microservicio.rrhh.models.entities.FormatoPermisos;
import com.microservicio.rrhh.models.enums.AttachmentType;
import com.microservicio.rrhh.models.enums.ValidateType;
import com.microservicio.rrhh.services.RrhhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@CrossOrigin(origins = { "*" })
@RestController
public class RrhhController {

   @Autowired
   private RrhhService service;

   @GetMapping(path = { "/findAllFormatoPermisos" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<FormatoPermisos>> findAllFormatoPermisos() {
      return Response
               .<List<FormatoPermisos>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.service.findAllFormatoPermisos())
               .build();
   }

   @PostMapping(path = { "/findFormatoPermisosByUsrCreador" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<FormatoPermisos>> findFormatoPermisosByUsrCreador(@RequestBody Usuario usrCreador) {
      return Response
               .<List<FormatoPermisos>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.service.findFormatoPermisosByUsrCreador(usrCreador))
               .build();
   }

   @PostMapping(value="/saveFormatoPermisos")
   @ResponseStatus(code = HttpStatus.OK)
   public Response<?> saveFormatoPermisos(@RequestBody FormatoPermisos formatoPermisos) {
      this.service.saveFormatoPermisos(formatoPermisos);
      return Response
               .builder()
               .message(Messages.MESSAGE_SUCCESS_CREATE)
               .data(List.of())
               .build();
   }

   @GetMapping(value="/downloadFormatoLicenciaById")
   public ResponseEntity<?> downloadFormatoLicenciaById(@RequestParam Long idFormato) {

      //» Dep's
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      String contentDisposition = "attachment; filename=\"Formato de Autorización %s %s.xlsx\"";

      //» Service dep's 
      FormatoPermisos formatoPermisos = this.service.findFormatoPermisosById(idFormato);
      String servidor = formatoPermisos.getNombres();
      Date fechaFormato = formatoPermisos.getFechaFormato();
      ByteArrayResource permisoTemplateToByteArrResource = this.service.convertPermisosTemplateToByteArrResource(idFormato);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, servidor, dateFormat.format(fechaFormato)));
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);

      return ResponseEntity
                  .ok()
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .headers(headers)
                  .body(permisoTemplateToByteArrResource);
   }

   @DeleteMapping(path= { "/deleteFormatoPermisosById" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> deleteFormatoPermisosById(@RequestParam Long idFormato) {
      this.service.deleteFormatoPermisosById(idFormato);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_DELETE_RECORD(idFormato))
               .data(List.of())
               .build();
   }

   @PutMapping(path= { "/validateFormatoPermisos" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> validateFormatoPermisos(@RequestParam Long idFormato, @RequestParam ValidateType type) {
      this.service.validateFormatoPermisos(idFormato, type);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_VALIDATE_RECORD_CTRLCAL)
               .data(List.of())
               .build();
   }
 
   @PostMapping(path = { "/uploadControlAsistencia" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<Long> uploadControlAsistencia(@RequestPart MultipartFile file) throws IOException {
       return Response
               .<Long>builder()
               .message(Messages.SUCCESS_UPLOAD_FILE)
               .data(this.service.saveAllControlAsistencia(file))
               .build();
   }
   
   @DeleteMapping(path = { "/deleteAllControlAsistencia" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<Long> deleteAllControlAsistencia() {
       return Response
               .<Long>builder()
               .message(Messages.SUCCESS_DELETE_TABLA("Asistencias"))
               .data(this.service.deleteAllControlAsistencia())
               .build();
   }

   @GetMapping(path = { "/findAllControlAsistenciaUsrs" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findAllControlAsistenciaUsrs() {
       return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.SUCCESS_UPLOAD_FILE)
               .data(this.service.findAllControlAsistenciaUsrs())
               .build();
   }

   @GetMapping(path = { "/countControlAsistencias" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<Long> countControlAsistencias() {
      return Response
               .<Long>builder()
               .message("")
               .data(this.service.countControlAsistencias())
               .build();
   }

   @GetMapping(path = { "/findControlPermisosByServidor" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findControlPermisosByServidor(@RequestParam String servidor) {
      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.MESSAGE_SUCCESS_DOWNLOAD)
               .data(this.service.findControlPermisosByServidor(servidor))
               .build();
   }

   @PutMapping(path = { "/uploadAttachment/{idFormato}/{type}" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<?> uploadAttachment(
                                 @RequestPart MultipartFile file, 
                                 @PathVariable AttachmentType type,
                                 @PathVariable Long idFormato) throws IOException {

       this.service.saveAttachment(file, type, idFormato);

       return Response
                  .builder()
                  .message(Messages.SUCCESS_UPLOAD_FILE)
                  .data(List.of())
                  .build();
   }

   @GetMapping(path = { "/downlodAttachment/{idFormato}/{type}" })
   @ResponseStatus(value = HttpStatus.OK)
   public ResponseEntity<?> downlodAttachment(@PathVariable AttachmentType type, @PathVariable Long idFormato) throws IOException {

      // » Dep's ...
       HttpHeaders headers = new HttpHeaders();
       headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
       headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);
       headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=".concat(type.name()).concat(".pdf"));

       return ResponseEntity
                     .ok()
                     .headers(headers)
                     .contentType(MediaType.APPLICATION_PDF)
                     .body(this.service.findAttachmentById(idFormato, type));
   }
   
   @PutMapping(path= { "/saveObservacionesFormatoPermisos" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> saveObservacionesFormatoPermisos(@RequestParam String observaciones, @RequestParam Long idFormato) {
      this.service.saveObservacionesFormatoPermisos(observaciones, idFormato);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_VALIDATE_RECORD_CTRLCAL)
               .data(List.of())
               .build();
   }

}