package com.microservicio.rimextraccion.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.constants.RimHttpHeaders;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.dto.RecordsBetweenDatesDto;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimanalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@CrossOrigin(origins = { "*" })
@RestController
public class RimanalisisController {

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired
   private RimasigGrupoCamposAnalisisService asigAnalisisService;

   @Autowired
   private RimanalisisService rimanalisisService;

   @Value("classpath:static/S10.DRCM.FR.001-Registro de depuracion de datos_V02.xlsx")
   private Resource rptProdAnalisisDiariaXlsx;
   
   @Value("classpatch:static/Ficha de reporte de producción v1.0.xlsx")
   private Resource rptProdAnalisisMensualXlsx;

   @PostMapping( path = { "/findAsigAnalisisByUsr" } )
   public ResponseEntity<?> findAsigAnalisisByUsr(@RequestBody Usuario usrAnalista) {
      List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisisDb = this.asigAnalisisService.findByUsrAnalista(usrAnalista);
      if (asigGrupoCamposAnalisisDb.size() == 0) 
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                 .data(asigGrupoCamposAnalisisDb)
                                 .build());
   }
   
   @GetMapping( path = { "/findTablaDinamicaByRangoFromIds" } )
   public ResponseEntity<?> findTablaDinamicaByRangoFromIds(
                                             @RequestParam String nombreTabla, 
                                             @RequestParam Long regAnalisisIni,
                                             @RequestParam Long regAnalisisFin){
      List<Map<String, Object>> tablaDinamicaDb = this.rimcommonService.findTablaDinamicaByRangoFromIds(nombreTabla, regAnalisisIni, regAnalisisFin);
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(tablaDinamicaDb)
                                       .build());
   }

   @PutMapping( path = { "/saveRecordAssigned" } )
   public ResponseEntity<?> saveRecordAssigned(@RequestBody RecordAssignedDto recordAssignedDto){

      // ► Save ...
      this.rimanalisisService.saveRecordAssigned(recordAssignedDto);

      // ► Record-Set ...
      List<Map<String, Object>> tdByRangoFromIds = this.rimcommonService.findTablaDinamicaByRangoFromIds(
                                                                     recordAssignedDto.getNombreTable(),
                                                                     recordAssignedDto.getRegAnalisisIni(),
                                                                     recordAssignedDto.getRegAnalisisFin());

      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.SUCCESS_SAVE_ANALISIS_EXTRACCION)
                                 .data(tdByRangoFromIds)
                                 .build());
   }

   @PostMapping( path = { "/downloadAnalisadosByDates" } )
   public ResponseEntity<?> downloadAnalisadosByDates(@RequestBody RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException {
         
      /* ► Dep's ... */
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      ByteArrayResource byteArrResource = null;
      String fileName = rptProdAnalisisDiariaXlsx.getFilename(),
             usrAnalista = this.asigAnalisisService.findById(recordsBetweenDatesDto.getIdAsigGrupo()).getUsrAnalista().getNombres();

      /* ► Header's ... */
      HttpHeaders headers = new HttpHeaders();
      String contentDisposition = String.format("attachment; filename=\"%s - %s(%s).xlsx\"", fileName, usrAnalista, df.format(new Date()));
      headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);

      byteArrResource = this.rimanalisisService.convertProduccionAnalisisToByteArrResource(recordsBetweenDatesDto);

      return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .headers(headers)
            .body(byteArrResource);

   }
 
   @GetMapping( path = { "/downloadReporteMensualProduccionByParams" } )
   public ResponseEntity<?> downloadReporteMensualProduccionByParams(@RequestParam String login, @RequestParam int month, @RequestParam int year) throws IOException {

      // Dep's ...
      String fileName = rptProdAnalisisMensualXlsx.getFilename().split("\\.")[0];
      String contentDisposition = "attachment; filename=\"%s\"";
      
      // Repo dep's ...
      Usuario usrAnalista = this.rimanalisisService.findUsuarioByLogin(login).getData();
      ByteArrayResource byteArrResourceRptMensualProduccion = this.rimanalisisService.convertReporteMensualProduccionToByteArrResource(login, month, year);
      
      
      // Header dep's ...
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, fileName.concat(" - ")
                                                                                             .concat(usrAnalista.getNombres())
                                                                                             .concat(".xlsx")));

      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);

      return ResponseEntity
               .ok()
               .headers(headers)
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .body(byteArrResourceRptMensualProduccion);
   }
  
}