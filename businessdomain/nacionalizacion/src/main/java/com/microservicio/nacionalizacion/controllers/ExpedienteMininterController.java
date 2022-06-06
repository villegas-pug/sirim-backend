package com.microservicio.nacionalizacion.controllers;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.nacionalizacion.models.dto.ExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.entities.DetExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;
import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;
import com.microservicio.nacionalizacion.services.ExpedienteMininterService;
import com.microservicio.nacionalizacion.services.NacionalizacionService;
import com.microservicio.nacionalizacion.services.UbicacionExpedienteService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = { "*" })
@RestController
public class ExpedienteMininterController {

   @Autowired
   private ExpedienteMininterService service;

   @Autowired
   private NacionalizacionService nacionalizacionService;

   @Autowired
   private UbicacionExpedienteService ubicacionMininterService;

   @Value("classpath:static/rpt_mininter.xlsx")
   private Resource templateRptMininter;

   @GetMapping(path = "/findByNumeroTramite/{numeroTramite}")
   public ResponseEntity<?> findByNumeroTramite(@PathVariable String numeroTramite) {

      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();

      /* » Find by número tramite in `Nacionalización` ... */
      Nacionalizacion nacionalizacion = this.nacionalizacionService
                                                .findByNumeroTramite(numeroTramite)
                                                .orElseThrow(DataAccessEmptyWarning::new);

      expedienteMininterDto.setNacionalizacion(nacionalizacion);

      /* » Find by número tramite in `ExpedienteMininter` ... */
      ExpedienteMininter expedienteMininter = this.service.findByNumeroExpediente(numeroTramite).orElse(null);

      expedienteMininterDto.setExpedienteMininter(expedienteMininter);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_SEARCH_RESULT(numeroTramite))
                  .data(Arrays.asList(expedienteMininterDto))
                  .build());
   }

   @PostMapping(path = "/saveExpedienteMininter/{numeroTramite}")
   public ResponseEntity<?> saveExpedienteMininter(
                                          @PathVariable String numeroTramite,
                                          @RequestBody Usuario usrDigita) {

      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();
      ExpedienteMininter newExpedienteMininter = new ExpedienteMininter();

      /* » STEP:01 ... */
      Nacionalizacion nacionalizacionDb = this.nacionalizacionService
                                                   .findByNumeroTramite(numeroTramite)
                                                   .orElseThrow(DataAccessEmptyWarning::new);

      newExpedienteMininter.setNumeroExpediente(nacionalizacionDb.getNumeroTramite());
      newExpedienteMininter.setTipoProcedimiento(nacionalizacionDb.getTipoTramite());
      newExpedienteMininter.setNombres(nacionalizacionDb.getAdministrado());
      newExpedienteMininter.setDependencia(nacionalizacionDb.getDependencia());
      newExpedienteMininter.setUsuarioDigita(usrDigita);

      this.service.save(newExpedienteMininter);

      expedienteMininterDto.setNacionalizacion(nacionalizacionDb);
      expedienteMininterDto.setExpedienteMininter(newExpedienteMininter);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_SAVE("El expediente"))
                  .data(Arrays.asList(expedienteMininterDto))
                  .build());
   }

   @PostMapping(path = "/saveDetExpedienteMininter/{numeroTramite}")
   public ResponseEntity<?> saveDetExpedienteMininter(
         @RequestBody DetExpedienteMininter detExpedienteMininter,
         @PathVariable String numeroTramite) {

      ExpedienteMininter expedienteMininter = this.service.findByNumeroExpediente(numeroTramite)
            .orElseThrow(() -> new DataAccessEmptyWarning());

      /* » New record ... */
      if (detExpedienteMininter.getIdDetExpMininter() == null)
         expedienteMininter.addDetExpedienteMininter(detExpedienteMininter);
      else/* » Edit record ... */
         expedienteMininter.replaceDetExpedienteMiniter(detExpedienteMininter);

      this.service.save(expedienteMininter);

      /* » Actualiza: Ubicación expediente, por la fecha de oficio más reciente ... */
      expedienteMininter.setUbicacion(expedienteMininter
            .getDetExpedienteMininter()
            .stream()
            .max(Comparator.comparing(DetExpedienteMininter::getFechaRecepcion))
            .get()
            .getUbicacion());

      ExpedienteMininter newExpedienteMininter = this.service.save(expedienteMininter);

      /* » Result: */
      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();
      expedienteMininterDto.setNacionalizacion(this.nacionalizacionService.findByNumeroTramite(numeroTramite).get());
      expedienteMininterDto.setExpedienteMininter(newExpedienteMininter);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_SAVE("El acta"))
                  .data(Arrays.asList(expedienteMininterDto))
                  .build());
   }

   @GetMapping(path = "/findAllUbicacionMininter")
   public ResponseEntity<?> findAllUbicacionMininter() {
      List<UbicacionExpediente> ubicacionMininterDb = this.ubicacionMininterService.findAll();
      if (ubicacionMininterDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(ubicacionMininterDb)
                  .build());
   }

   @DeleteMapping(path = "/removeDetExpedienteMininter/{numeroTramite}")
   public ResponseEntity<?> removeDetExpedienteMininter(
                                       @RequestBody EntitiesDto entitiesDto,
                                       @PathVariable String numeroTramite) {

      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();

      Nacionalizacion nacionalizacion = this.nacionalizacionService
                                                .findByNumeroTramite(numeroTramite)
                                                .orElseThrow(() -> new DataAccessEmptyWarning());
      expedienteMininterDto.setNacionalizacion(nacionalizacion);

      ExpedienteMininter expedienteMininter = this.service.findByNumeroExpedienteAndUsuarioDigita(numeroTramite, entitiesDto.usuario).get();
      expedienteMininter.removeDetExpedienteMininter(entitiesDto.detExpedienteMininter);
      this.service.save(expedienteMininter);

      expedienteMininterDto.setExpedienteMininter(expedienteMininter);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(entitiesDto.detExpedienteMininter.getIdDetExpMininter()))
                  .data(Arrays.asList(expedienteMininterDto))
                  .build());
   }

   @PostMapping(path = "/findByUbicacion", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
   public ResponseEntity<?> findByUbicacion(@RequestBody UbicacionExpediente ubicacion) {

      List<ExpedienteMininter> expedienteMininterDb = this.service.findByUbicacion(ubicacion);
      HttpHeaders headers = new HttpHeaders();

      if (expedienteMininterDb.size() == 0) {
         headers.add(MediaType.TEXT_HTML_VALUE, Messages.MESSAGGE_WARNING_EMPTY());
         return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .headers(headers)
               .body(null);
      }

      try (XSSFWorkbook bookMininter = new XSSFWorkbook(templateRptMininter.getInputStream())) {

         XSSFSheet sheetMininter = bookMininter.getSheetAt(0);
         int i = 0;
         for (ExpedienteMininter record : expedienteMininterDb) {
            i++;
            XSSFRow rowMininter = sheetMininter.getRow(i);
            rowMininter.getCell(0).setCellValue(i);
            rowMininter.getCell(1).setCellValue(record.getNumeroExpediente());
            rowMininter.getCell(2).setCellValue(record.getFechaRegistro());
            rowMininter.getCell(3).setCellValue(record.getNombres());
            rowMininter.getCell(4).setCellValue(record.getTipoProcedimiento());
            rowMininter.getCell(5).setCellValue(record.getDependencia());
            rowMininter.getCell(6).setCellValue(record.getUbicacion().getDescripcion());
         }

         ByteArrayOutputStream outputMininter = new ByteArrayOutputStream();
         bookMininter.write(outputMininter);
         Resource xlsxMininter = new ByteArrayResource(outputMininter.toByteArray());

         

         String contentDisposition = "attachment; filename=%s";
         headers.add(
               HttpHeaders.CONTENT_DISPOSITION,
               String.format(contentDisposition, templateRptMininter.getFilename()));

         return ResponseEntity
               .ok()
               .headers(headers)
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .body(xlsxMininter);
      } catch (Exception e) {
         return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .body(null);
      }
   }

   public static class EntitiesDto {
      public DetExpedienteMininter detExpedienteMininter;
      public Usuario usuario;
   }

}