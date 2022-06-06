package com.microservicio.nacionalizacion.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.dto.IndicadoresCeremoniaDto;
import com.commons.utils.utils.Response;
import com.microservicio.nacionalizacion.models.entities.Ceremonia;
import com.microservicio.nacionalizacion.services.CeremoniaService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = { "*" })
@RestController
public class CeremoniaController {

   private static final String CEREMONIA = "Ceremonia";

   @Autowired
   private CeremoniaService service;

   @PostMapping(path = "/saveAllCeremonia", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
   public ResponseEntity<?> saveAllCeremonia(
                                 @RequestPart Ceremonia ceremonia,
                                 @RequestPart MultipartFile file) throws IOException {

      List<Ceremonia> newCeremonia = new ArrayList<>();

      try (XSSFWorkbook book = new XSSFWorkbook(file.getInputStream())) {

         XSSFSheet sheet = book.getSheet(CEREMONIA);

         int rowsSheet = sheet.getPhysicalNumberOfRows();

         for (int i = 3; i <= rowsSheet; i++) {
            
            Ceremonia recordCeremonia = new Ceremonia();

            XSSFRow row = sheet.getRow(i);

            /*» CASTING CELL'S TYPE: */
            for (int c = 0; c < 18; c++) {
               if(row.getCell(c) != null)
                  if (c != 6) row.getCell(c).setCellType(CellType.STRING);
            }

            /*» VALIDA: Si, fila celda devuelve nulo interrumpe el loop ...  */
            if(row.getCell(0) == null) break;

            recordCeremonia.setLugarNacimiento(row.getCell(1).getStringCellValue());
            recordCeremonia.setNacionalidad(row.getCell(2).getStringCellValue());
            recordCeremonia.setReligion(row.getCell(3).getStringCellValue());
            recordCeremonia.setCargo(row.getCell(4).getStringCellValue());
            recordCeremonia.setNombres(row.getCell(5).getStringCellValue());
            recordCeremonia.setFechaNacimiento(row.getCell(6).getDateCellValue());
            recordCeremonia.setEdad(row.getCell(7).getStringCellValue());
            recordCeremonia.setMotivo(row.getCell(8).getStringCellValue());
            recordCeremonia.setEmail(row.getCell(9).getStringCellValue());
            recordCeremonia.setTelefono(row.getCell(10).getStringCellValue());
            recordCeremonia.setNumeroExpediente(row.getCell(11).getStringCellValue());
            recordCeremonia.setAñosResidenciaPeru(row.getCell(12).getStringCellValue());
            recordCeremonia.setProfesion(row.getCell(13).getStringCellValue());

            /* recordCeremonia.setConyuge(row.getCell(14).getStringCellValue());
            recordCeremonia.setDni(row.getCell(15).getStringCellValue());
            recordCeremonia.setNumeroHijosPeruano(row.getCell(16).getStringCellValue());
            recordCeremonia.setEmpresa(row.getCell(17).getStringCellValue());*/

            recordCeremonia.setFechaCeremonia(ceremonia.getFechaCeremonia());

            newCeremonia.add(recordCeremonia);
         }

         /*» RESPONSE-ENTITY: */
         this.service.saveAll(newCeremonia);
         List<IndicadoresCeremoniaDto> indicadoresCeremoniaDto = this.service.getSumarizzeCeremonia();

         return ResponseEntity.ok().body(
                                       Response
                                          .builder()
                                          .message(Messages.MESSAGE_SUCCESS_SAVE("Ceremonia"))
                                          .data(indicadoresCeremoniaDto)
                                          .build());
      } catch (Exception err) {
         return ResponseEntity.ok().body(
                                       Response
                                          .builder()
                                          .levelLog(LevelLog.WARNING)
                                          .message(Messages.MESSAGE_WARNING_DATA_SAVE)
                                          .data(Arrays.asList())
                                          .build());
      }

   }

   @GetMapping(path="/getSumarizzeCeremonia")
   public ResponseEntity<?> getSumarizzeCeremonia() {
      List<IndicadoresCeremoniaDto> indicadoresCeremoniaDto = this.service.getSumarizzeCeremonia();
      if (indicadoresCeremoniaDto.size() == 0) throw new DataAccessEmptyWarning();
      
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(indicadoresCeremoniaDto)
                                       .build());
   }

}