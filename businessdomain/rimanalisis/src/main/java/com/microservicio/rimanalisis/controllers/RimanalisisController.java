package com.microservicio.rimanalisis.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.RimHttpHeaders;
import com.commons.utils.models.dto.RecordsBetweenDatesDto;
import com.commons.utils.models.dto.RptProduccionHoraLaboralDto;
import com.commons.utils.models.dto.RptTiempoPromedioAnalisisDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.models.enums.RimGrupo;
import com.commons.utils.utils.Response;
import com.microservicio.rimanalisis.services.RimanalisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin(origins = { "*" })
@RestController
public class RimanalisisController {

   @Autowired
   private RimanalisisService rimanalisisService;
   
   @Value("classpatch:static/Ficha de reporte de producción v1.0.xlsx")
   private Resource rptProdAnalisisMensualXlsx;

   @PostMapping( path = { "/downloadAnalisadosByDates" } )
   public ResponseEntity<?> downloadAnalisadosByDates(@RequestBody RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException {
         
      // ► Dep's ...
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      boolean isRequestOfRoot = recordsBetweenDatesDto.getIdAsigGrupo() == null ? true : false;
      ByteArrayResource byteArrResource = null;
      Usuario usr;

      // ► Repo dep's ...
      if (isRequestOfRoot) // ► Petición del coordinador ...
         usr = recordsBetweenDatesDto.getUsr();
      else // ► Petición del analista ...
         usr = this.rimanalisisService
                        .findAsigGrupoCamposAnalisisById(recordsBetweenDatesDto.getIdAsigGrupo())
                        .getUsrAnalista();

      String fileName = usr.getGrupo() == RimGrupo.DEPURACION
                                             ? "S10.DRCM.FR.001-Registro de depuracion de datos_V02"
                                             : "S10.DRCM.FR.002-Registro para el analisis de informacion_V02";

      /* ► Header's ... */
      HttpHeaders headers = new HttpHeaders();
      String contentDisposition = String.format("attachment; filename=\"%s_%s_%s.xlsx\"", fileName, usr.getNombres(), df.format(new Date()));
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
   @SuppressWarnings(value = { "null" })
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
 
   @GetMapping( path = { "/getRptTiempoPromedioAnalisisByParms" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptTiempoPromedioAnalisisDto>> getRptTiempoPromedioAnalisisByParms(
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni, 
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin) {
      return Response
               .<List<RptTiempoPromedioAnalisisDto>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.rimanalisisService.getRptTiempoPromedioAnalisisByParms(fecIni, fecFin))
               .build();
   }
   
   @GetMapping(path = { "/getRptProduccionHorasLaboralesPorAnalista" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<RptProduccionHoraLaboralDto>> getRptProduccionHorasLaboralesPorAnalista(
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaAnalisis,
                                                            @RequestParam String grupo) {
       return Response
                  .<List<RptProduccionHoraLaboralDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimanalisisService.getRptProduccionHorasLaboralesPorAnalista(fechaAnalisis, grupo))
                  .build();
   }
   
   @PutMapping(path = { "/setTerminadoProduccionAnalisis/{idProdAnalisis}" })
   public Response<List<?>> setTerminadoProduccionAnalisis(@PathVariable Long idProdAnalisis) {
      this.rimanalisisService.setTerminadoProduccionAnalisis(idProdAnalisis);
      return Response
                  .<List<?>>builder()
                  .message(Messages.SUCCESS_SAVE_ANALISIS_EXTRACCION)
                  .data(List.of())
                  .build();
   }

}