package com.microservicios.operativo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.entities.ExpedienteSolicitudSFM;
import com.microservicios.operativo.services.ExpedienteSolicitudSFMService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin( origins = { "*" } )
@RestController
@Log4j2
public class ExpedienteSolicitudSFMController {
   
   @Autowired
   private ExpedienteSolicitudSFMService service;

   @PostMapping( path = { "/saveFileExpedienteSolicitud" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
   public ResponseEntity<?> saveFileExpedienteSolicitud(@RequestPart MultipartFile file){

      List<ExpedienteSolicitudSFM> lstExpedienteSolicitudSFM = new ArrayList<>();

      try (XSSFWorkbook book = new XSSFWorkbook(file.getInputStream())) {
         
         int sheets = book.getNumberOfSheets();

         for (int i = 0; i < sheets; i++) {/*» Recorre `sheets` ...  */
            XSSFSheet sheet = book.getSheetAt(i);

            int rows = sheet.getPhysicalNumberOfRows();

            for (int j = 8; j < rows; j++) {/*» Recorre `rows` ...  */
               
               if(sheet.getRow(j) == null) break;/*» Si, fila es nula, interrumpe iteracción ...  */

               XSSFRow row = sheet.getRow(j);

               if (row.getCell(3) == null) break;/*» Si, celda es nula, `-` o ``, interrumpe iteracción ...  */
               else if(row.getCell(3).getStringCellValue().trim().equals("-") 
                        || row.getCell(3).getStringCellValue().trim().isEmpty()) break;

               lstExpedienteSolicitudSFM.add(
                                          ExpedienteSolicitudSFM
                                          .of()
                                          .numeroExpediente(row.getCell(3).getStringCellValue())
                                          .fechaAprobacion(row.getCell(4).getDateCellValue())
                                          .procedimiento(row.getCell(5).getStringCellValue())
                                          .administrado(row.getCell(6).getStringCellValue())
                                          .nacionalidad(row.getCell(7).getStringCellValue())
                                          .domicilio(row.getCell(8).getStringCellValue())
                                          .distrito(row.getCell(9).getStringCellValue())
                                          .fileName(file.getOriginalFilename())
                                          .get());
            }

         }

         this.service.saveAll(lstExpedienteSolicitudSFM);

         return ResponseEntity
                        .ok()
                        .body(
                           Response
                              .builder()
                              .message(Messages.SUCCESS_UPLOAD_FILE)
                              .data(this.service.getMetadataFilesExpedienteSolicitud())
                              .build());

      } catch (Exception e) {
         log.error(e.getMessage());
         return ResponseEntity
                        .ok()
                        .body(
                           Response
                              .builder()
                              .message(Messages.ERROR_FILE_SAVE(file.getOriginalFilename()))
                              .data(Arrays.asList())
                              .build());
      }
   }

   @GetMapping( path = { "/findAllMetaOfExpediente" } )
   public ResponseEntity<?> findAllMetaOfExpediente(){

      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(this.service.getMetadataFilesExpedienteSolicitud())
            .build());
   }
   
   @GetMapping( path = "/findByNumeroExpediente" )
   public ResponseEntity<?> findByNumeroExpediente(@RequestParam String numeroExpediente) {

      ExpedienteSolicitudSFM expedienteSolicitudSFM = this.service
                                                            .findByNumeroExpediente(numeroExpediente)
                                                            .orElseThrow(DataAccessEmptyWarning::new);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SEARCH_RESULT(numeroExpediente))
                                       .data(expedienteSolicitudSFM)
                                       .build());
   }
   
   @PostMapping( path = { "/saveExpedienteSolicitud" }, consumes = { MediaType.APPLICATION_JSON_VALUE } )
   public ResponseEntity<?> saveExpedienteSolicitud(@RequestBody ExpedienteSolicitudSFM expedienteSolicitud){
      this.service.save(expedienteSolicitud);
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SAVE)
                                       .data(Arrays.asList())
                                       .build());
   }

}