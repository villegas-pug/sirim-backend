package com.microservicios.interpol.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.controllers.CommonController;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.EntityFindByIdWarning;
import com.commons.utils.utils.Response;
import com.microservicios.interpol.models.entity.Interpol;
import com.microservicios.interpol.services.InterpolService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "*" })
@RestController
@Log4j2
public class InterpolController extends CommonController<Interpol, InterpolService> {

   @PostMapping(path = "/findByApprox")
   public ResponseEntity<?> findByApprox(@RequestBody Interpol interpol) {

      List<Interpol> interpoDb = super.service.findByAppox(interpol.getNombres(), interpol.getApellidos(),
            interpol.getCedula(), interpol.getPasaporte());

      if (interpoDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok()
         .body(
            Response
               .builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
               .data(interpoDb)
               .build());
   }

   @PostMapping(path = "/saveByOnlyOne", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
   public ResponseEntity<?> saveByOnlyOne(@RequestPart Interpol interpol, 
                                          @RequestPart MultipartFile file) throws IOException {
      
      interpol.setScreenShot(file.getBytes());
      this.service.save(interpol);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SAVE("La ficha de interpol"))
                                       .data(Arrays.asList())
                                       .build());
   }
   
   @GetMapping(path = "/downloadScreenshot", produces = { MediaType.IMAGE_PNG_VALUE })
   public ResponseEntity<?> downloadScreenshot(@RequestParam Long idInterpol) throws IOException {

      Interpol interpol = this.service.findById(idInterpol).orElseThrow(() -> 
                              new EntityFindByIdWarning(idInterpol));

      Resource screeShot = new ByteArrayResource(interpol.getScreenShot());

      String contentDisposition = "attachment; filename=\"%s.png\"";
      HttpHeaders headers = new HttpHeaders();
      headers.add(
            HttpHeaders.CONTENT_DISPOSITION, 
            String.format(contentDisposition, interpol.getNombres().concat(", ").concat(interpol.getApellidos())));

      return ResponseEntity
               .ok()
               .headers(headers)
               .contentType(MediaType.IMAGE_PNG)
               .body(screeShot);
   }

   @PostMapping(path = "/saveAll", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
   public ResponseEntity<?> saveAll(@RequestPart MultipartFile file) {

      try (XSSFWorkbook book = new XSSFWorkbook(file.getInputStream())) {
         
         List<Interpol> newInterpol = new ArrayList<Interpol>();
         XSSFSheet sheet = book.getSheetAt(0);

         int initRow = 1;
         for (int i = initRow; i < sheet.getPhysicalNumberOfRows(); i++) {

            if (sheet.getRow(i) == null) break;

            XSSFRow row = sheet.getRow(i);

            /*»Valida: Si, la indice es nulo, interrumpe la iteracción...  */
            if (row.getCell(0) == null) break;

            /*» Cast: ...  */
            for (int j = 1; j <= 11; j++)
               if (row.getCell(j) != null) row.getCell(j).setCellType(CellType.STRING);

            newInterpol.add(new Interpol(
                              convertCellNullableToString(row.getCell(2)), 
                              convertCellNullableToString(row.getCell(1)), 
                              convertCellNullableToString(row.getCell(3)), 
                              convertCellNullableToString(row.getCell(4)), 
                              convertCellNullableToString(row.getCell(5)), 
                              convertCellNullableToString(row.getCell(6)), 
                              convertCellNullableToString(row.getCell(7)), 
                              convertCellNullableToString(row.getCell(9)), 
                              convertCellNullableToString(row.getCell(10)), 
                              convertCellNullableToString(row.getCell(8)), 
                              convertCellNullableToString(row.getCell(11))));
         }

         super.service.saveAll(newInterpol);
      } catch (Exception e) {
         log.error(e.getMessage());
         return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .levelLog(LevelLog.ERROR)
                                       .message(e.getMessage())
                                       .data(Arrays.asList())
                                       .build());
      }

      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_SAVE("Interpol"))
            .data(Arrays.asList())
            .build());
   }

   @GetMapping(path = "/testFindAll")
   public ResponseEntity<?> testFindAll() {
       return ResponseEntity.ok().body(
          Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
            .data(super.service.testFindAll())
            .build()
       );
   }

   private String convertCellNullableToString(XSSFCell cell){
      if (cell != null) cell.setCellType(CellType.STRING);
      return cell != null ? cell.getStringCellValue() : "";
   }
}