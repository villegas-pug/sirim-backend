package com.microservicio.produccion.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.controllers.CommonController;
import com.commons.utils.errors.UserNotFoundWarning;
import com.commons.utils.models.dto.DetalleProduccionDto;
import com.commons.utils.models.dto.ProduccionSemanalDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.produccion.models.entities.DetalleProduccion;
import com.microservicio.produccion.models.entities.Produccion;
import com.microservicio.produccion.services.ProduccionService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = { "*" }, exposedHeaders = { "*" })
@RestController
public class ProduccionController extends CommonController<Produccion, ProduccionService> {

   private Date today = new Date();

   @PostMapping(path = "/registerActividad")
   public ResponseEntity<?> registerActividad(@RequestBody DetalleProduccionDto detProduccion, @RequestParam String userAuth) {

      Usuario user = this.service.findByUserAuth(userAuth).orElseThrow(() -> new UserNotFoundWarning(userAuth));

      Produccion newProduccion = new Produccion();
      DetalleProduccion newDetProduccion = new DetalleProduccion();

      newDetProduccion.setDescripcionActividad(detProduccion.getDescripcionActividad());
      newDetProduccion.setAccionDesarrollada(detProduccion.getAccionDesarrollada());

      newProduccion.setUsuario(user);
      newProduccion.setGradoAvanceDiarioEjecutado(detProduccion.getGradoAvanceDiarioEjecutado());
      newProduccion.addDetalleProduccion(newDetProduccion);

      super.service.save(newProduccion);

      return ResponseEntity.ok().body(
         Response
         .builder()
         .message(Messages.MESSAGE_SUCCESS_CREATE)
         .data(super.service.findByUsuarioAndFechaRegistro(user, this.today))
         .build()
      );
   }

   @GetMapping(path = "/findByUserAuth/{userAuth}")
   public ResponseEntity<?> findByUserAuth(@PathVariable String userAuth) {
       return ResponseEntity.ok().body(this.service.findByUserAuth(userAuth));
   }

   @DeleteMapping(path = "/deleteById")
   public ResponseEntity<?> deleteById(@RequestParam Long idProd, @RequestParam String userAuth){
      super.service.deleteById(idProd);

      Usuario usuario = super.service.findByUserAuth(userAuth)
                                       .orElseThrow(() -> new UserNotFoundWarning(userAuth));

      List<Produccion> produccionDb = super.service.findByUsuarioAndFechaRegistro(usuario, this.today);

      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(idProd))
            .data(produccionDb)
            .build()
      );
   }

   @GetMapping(path = "/countActividadCurrentWeek")
   public ResponseEntity<?> countActividadCurrentWeek(@RequestParam String userAuth) {
      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(super.service.countActividadCurrentWeek(userAuth))
            .build()
      );
   }

   @GetMapping(path = "/countActividadSemanalByDate")
   public ResponseEntity<?> countActividadSemanalByDate(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date refDate) throws IOException {
         
      int daysOfWeeks = 5;
      LocalDate localDate = refDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

      List<ProduccionSemanalDto> produccionSemanalDto = super.service.countActividadSemanalByDate(refDate);
      if(produccionSemanalDto.size() == 0)
         return ResponseEntity
                  .status(HttpStatus.NO_CONTENT)
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(null);

      LocalDate firstDayOfWeekDate = localDate.minusDays(localDate.getDayOfWeek().getValue() - 1);
      LocalDate lastDayOfWeekDate = firstDayOfWeekDate.plusDays(daysOfWeeks - 1);

       
      String firstDayOfWeekDateStr = firstDayOfWeekDate.format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
      String lastDayOfWeekDateStr = lastDayOfWeekDate.format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));

      Resource template = new ClassPathResource("static/S01.RH.FR.050 Reporte Semanal de Actividades de TR_V01.xlsx");
      
      try (XSSFWorkbook book = new XSSFWorkbook(template.getInputStream())) {
         XSSFSheet sheet = book.getSheetAt(0);
         sheet.getRow(4).getCell(9).setCellValue(
            String.format("Del %s Al %s", firstDayOfWeekDateStr, lastDayOfWeekDateStr));
               
         produccionSemanalDto.forEach(prodByUser -> {
            XSSFRow rowBody = sheet.createRow(prodByUser.getId() + 7);
            rowBody.createCell(0).setCellValue(prodByUser.getId());
            rowBody.createCell(1).setCellValue(prodByUser.getNombres());
            rowBody.createCell(2).setCellValue(prodByUser.getDni());
            rowBody.createCell(3).setCellValue(prodByUser.getCargo());
            rowBody.createCell(4).setCellValue(prodByUser.getRegimenLaboral());
            rowBody.createCell(5).setCellValue(prodByUser.getDescripcionActividad());
            rowBody.createCell(6).setCellValue(prodByUser.getAccionDesarrollada());
            rowBody.createCell(7).setCellValue("100%");
            rowBody.createCell(8).setCellValue(String.valueOf(prodByUser.getGradoAvanceDiarioEjecutado()).concat("%"));
            rowBody.createCell(9).setCellValue(prodByUser.getGradoAvanceDiarioEjecutado() == 100 ? "Si" : "No");
            rowBody.createCell(10).setCellValue(prodByUser.getObservaciones());
         });
         
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         book.write(output);
         
         String contentDisposition = "attachment; filename=\"%s Semana, del %s al %s.xlsx\"";
         HttpHeaders header = new HttpHeaders();
         header.add(
            HttpHeaders.CONTENT_DISPOSITION, 
            String.format(contentDisposition, template.getFilename().split(".xls")[0], firstDayOfWeekDateStr, lastDayOfWeekDateStr));

         return ResponseEntity.ok()
            .headers(header)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new ByteArrayResource(output.toByteArray()));
      } 
   }
}