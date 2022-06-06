package com.microservicio.nacionalizacion.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.typeFile;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.FileSaveWarnning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.nacionalizacion.models.dto.DetExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.dto.ExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.dto.PlazosOficioRptDto;
import com.microservicio.nacionalizacion.models.entities.DetExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.MailFile;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;
import com.microservicio.nacionalizacion.services.ExpedienteMininterService;
import com.microservicio.nacionalizacion.services.MailFileService;
import com.microservicio.nacionalizacion.services.NacionalizacionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;

@Log4j2
@CrossOrigin(origins = { "*" })
@RestController
public class OficioController {

   @Autowired
   private NacionalizacionService nacionalizacionService;

   @Autowired
   private ExpedienteMininterService expedienteMininterService;
   
   @Autowired
   private MailFileService mailFileService;

   @Value("classpath:static/rpt_plazos_oficios_mininter.xlsx")
   private Resource templateOficiosMininter;

   @PostMapping(path = "/findByNumeroTramiteAndUsrDig/{numeroTramite}")
   public ResponseEntity<?> findByNumeroTramiteAndUsrDig(
                                 @PathVariable String numeroTramite,
                                 @RequestBody Usuario usrDigita) {

      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();

      /* » Buscar por número trámite en `Nacionalización` ... */
      Nacionalizacion nacionalizacion = this.nacionalizacionService
                                                .findByNumeroTramite(numeroTramite)
                                                .orElseThrow(DataAccessEmptyWarning::new);
      expedienteMininterDto.setNacionalizacion(nacionalizacion);

      /* » Buscar por número tramite in `ExpedienteMininter` ... */
      ExpedienteMininter expedienteMininter = this.expedienteMininterService
                                             .findByNumeroExpedienteAndUsuarioDigita(numeroTramite, usrDigita)
                                             .orElse(null);
      expedienteMininterDto.setExpedienteMininter(expedienteMininter);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SEARCH_RESULT(numeroTramite))
                                       .data(Arrays.asList(expedienteMininterDto))
                                       .build());
   }

   @PostMapping(path = "/saveOficioInExpediente/{numeroTramite}")
   public ResponseEntity<?> saveOficioInExpediente(
                                       @RequestBody EntitiesDto entitiesDto,
                                       @PathVariable String numeroTramite) {

      ExpedienteMininter expedienteMininter = this.expedienteMininterService
                                                      .findByNumeroExpedienteAndUsuarioDigita(numeroTramite, entitiesDto.usuario)
                                                      .orElseThrow(DataAccessEmptyWarning::new);

      /* » Si, no incluye `idDetExpediente`, se registra como nuevo ... */
      if (entitiesDto.detExpedienteMininter.getIdDetExpMininter() == null)
            expedienteMininter.addDetExpedienteMininter(entitiesDto.detExpedienteMininter);
      else/* » Edit record ... */
            expedienteMininter.replaceDetExpedienteMiniter(entitiesDto.detExpedienteMininter);

      Nacionalizacion expedienteNacionalizacion = this.nacionalizacionService
                                                         .findByNumeroTramite(numeroTramite)
                                                         .orElseThrow(DataAccessEmptyWarning::new);
      ExpedienteMininter newExpedienteMininter = this.expedienteMininterService.save(expedienteMininter);

      /* » Response-Entity ... */
      ExpedienteMininterDto expedienteMiniterDto = new ExpedienteMininterDto();
      expedienteMiniterDto.setNacionalizacion(expedienteNacionalizacion);
      expedienteMiniterDto.setExpedienteMininter(newExpedienteMininter);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SAVE("El oficio"))
                                       .data(Arrays.asList(expedienteMiniterDto))
                                       .build());
   }

   @PostMapping(path = "/saveMailFileInOficio", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
   public ResponseEntity<?> saveMailFileInOficio(
                                    @RequestPart EntitiesDto entitiesDto,
                                    @RequestPart MultipartFile file) throws IOException {
      
      String originalFileName = file.getOriginalFilename();
      String fileName = originalFileName.split("\\.")[0];
      String typeOfFile = originalFileName.split("\\.")[1];

      /*» Validación: Tipo de archivo soportado ...  */
      if (!typeOfFile.equalsIgnoreCase(typeFile.MSG)) throw new FileSaveWarnning(typeOfFile);

      /*» Lista todos `ExpedienteMininter` ... */
      List<ExpedienteMininter> expedienteMininterDb = this.expedienteMininterService.findAll();
      if(expedienteMininterDb.isEmpty()) throw new DataAccessEmptyWarning();

      /*» ...  */
      MailFile mailFile = new MailFile();
      mailFile.setDescripcion(fileName);
      mailFile.setFile(file.getBytes());

      /*» Defined `Response-Entity` ... */
      ExpedienteMininterDto expedienteMininterDto = new ExpedienteMininterDto();

      /*» Filtra y actualiza `ExpedienteMininter` por `idDetExpMininter` ... */
      ExpedienteMininter updatedExpedienteMininter = expedienteMininterDb
                                                         .stream()
                                                         .filter(expMininter -> {
                                                            return expMininter
                                                               .getDetExpedienteMininter()
                                                                           .stream()
                                                                           .filter(entitiesDto.detExpedienteMininter::equals)
                                                                           .peek(record -> record.addMailFile(mailFile))
                                                                           .findFirst()
                                                                           .isPresent();
                                                         })
                                                         .peek(record -> {
                                                                  expedienteMininterDto.setNacionalizacion(
                                                                     this.nacionalizacionService
                                                                                          .findByNumeroTramite(record.getNumeroExpediente())
                                                                                          .orElseThrow(DataAccessEmptyWarning::new));
                                                         })
                                                         .findFirst()
                                                         .get();
                                 
      /*» Save and Response-Entity ... */
      ExpedienteMininter newExpedienteMininter = this.expedienteMininterService.save(updatedExpedienteMininter);
      Nacionalizacion nacionalizacion = this.nacionalizacionService
                                                .findByNumeroTramite(entitiesDto.nacionalizacion.getNumeroTramite())
                                                .orElseThrow(DataAccessEmptyWarning::new);
      expedienteMininterDto.setExpedienteMininter(newExpedienteMininter);
      expedienteMininterDto.setNacionalizacion(nacionalizacion);
                                          
      return ResponseEntity.ok(
                        Response
                           .builder()
                           .message(Messages.MESSAGE_SUCCESS_SAVE("El oficio"))
                           .data(Arrays.asList(expedienteMininterDto))
                           .build());
   }

   @GetMapping(path = "/downloadMailFile/{idMailFile}", produces = { MediaType.ALL_VALUE })
   public ResponseEntity<?> downloadMailFile(@PathVariable Long idMailFile) {
      
      List<ExpedienteMininter> expedienteMininterDb = this.expedienteMininterService.findAll();
      if (expedienteMininterDb.isEmpty()) throw new DataAccessEmptyWarning();
      
      EntitiesDto entitiesDto = new EntitiesDto();
      
      expedienteMininterDb
               .stream()
               .filter(expeMininter -> expeMininter
                                          .getDetExpedienteMininter()
                                          .stream()
                                          .filter(detExpMininter -> {
                                                return detExpMininter
                                                         .getMailFiles()
                                                         .stream()
                                                         .filter(mailFile -> mailFile.getIdMailFile() == idMailFile)
                                                         .peek(record -> entitiesDto.mailFile = record)
                                                         .findFirst()
                                                         .isPresent();
                                          })
                                          .findFirst()
                                          .isPresent()
               )
               .findFirst();
      
      /*» Response-Entity ... */
      Resource mailFile = new ByteArrayResource(entitiesDto.mailFile.getFile());

      String contentDisposition = "attachment; filename=\"%s.msg\"";
      HttpHeaders headers = new HttpHeaders();
      headers.add(
         HttpHeaders.CONTENT_DISPOSITION, 
         String.format(contentDisposition, entitiesDto.mailFile.getDescripcion()));

      return ResponseEntity
                     .ok()
                     .headers(headers)
                     .contentType(MediaType.APPLICATION_XHTML_XML)
                     .body(mailFile);
   }

   @PostMapping(path = "/findAllPlazosOficio")
   public ResponseEntity<?> findAllPlazosOficio(@RequestBody Usuario usuario) {

      List<DetExpedienteMininterDto> detExpedienteMininterDto = this.expedienteMininterService.findAllFueraPlazoOficio(usuario.getIdUsuario());
      if(detExpedienteMininterDto.size() == 0) throw new DataAccessEmptyWarning();

      PlazosOficioRptDto plazosOficioRptDto = new PlazosOficioRptDto();
      plazosOficioRptDto.setDentroPlazoVenc(detExpedienteMininterDto.stream().filter((record) -> record.getEstadoPlazo() == 1).count());
      plazosOficioRptDto.setProximoPlazoVenc(detExpedienteMininterDto.stream().filter((record) -> record.getEstadoPlazo() == 0).count());
      plazosOficioRptDto.setFueraPlazoVenc(detExpedienteMininterDto.stream().filter((record) -> record.getEstadoPlazo() == -1).count());

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(Arrays.asList(plazosOficioRptDto))
                                       .build());
   }
  
   @PostMapping(path = "/downloadPlazosOficio/{estadoPlazo}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
   public ResponseEntity<?> downloadPlazosOficio(
                                       @RequestBody Usuario usuario,
                                       @PathVariable Long estadoPlazo) {

      List<DetExpedienteMininterDto> detExpedienteMininterDtoDb = this.expedienteMininterService.findAllFueraPlazoOficio(usuario.getIdUsuario());
      if(detExpedienteMininterDtoDb.size() == 0) throw new DataAccessEmptyWarning();

      Map<Long, String> plasos = new HashMap<>();
      plasos.put(1L, "Dentro Plazo");
      plasos.put(0L, "Proximo Vencimiento");
      plasos.put(-1L, "Fuera Plazo");

      List<DetExpedienteMininterDto> filterDetExpedienteMininterDtoDb = detExpedienteMininterDtoDb
                                                                           .stream()
                                                                           .filter((record) -> record.getEstadoPlazo() == estadoPlazo)
                                                                           .collect(Collectors.toList());

      try (XSSFWorkbook workbook = new XSSFWorkbook(this.templateOficiosMininter.getInputStream())) {
         XSSFSheet sheet = workbook.getSheetAt(0);
         
         int i = 1;
         for (DetExpedienteMininterDto detExpedienteMininter : filterDetExpedienteMininterDtoDb) {
            XSSFRow row = sheet.getRow(i);
            row.getCell(0).setCellValue(i);
            row.getCell(1).setCellValue(detExpedienteMininter.getNumeroExpediente());
            row.getCell(2).setCellValue(detExpedienteMininter.getNumeroOficio());
            row.getCell(3).setCellValue(detExpedienteMininter.getFechaRecepcion());
            row.getCell(4).setCellValue(detExpedienteMininter.getFechaRegistro());
            row.getCell(5).setCellValue(detExpedienteMininter.getNumeroOficio());
            row.getCell(6).setCellValue(detExpedienteMininter.getEstado());
            row.getCell(7).setCellValue(detExpedienteMininter.getFechaRespuesta());
            row.getCell(8).setCellValue(detExpedienteMininter.getFechaVencimiento());
            row.getCell(9).setCellValue(detExpedienteMininter.getAccionesRealizadas());
            i++;
         }

         ByteArrayOutputStream output = new ByteArrayOutputStream();
         workbook.write(output);

         String contentDisposition = "attachment; filename=\"Reporte de oficios %s.xlsx\"";
         HttpHeaders headers = new HttpHeaders();
         headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, plasos.get(estadoPlazo)));

         return ResponseEntity
                           .ok()
                           .headers(headers)
                           .contentType(MediaType.APPLICATION_OCTET_STREAM)
                           .body(new ByteArrayResource(output.toByteArray()));


      } catch (Exception e) {
         log.error(e.getMessage());
         return ResponseEntity
                           .status(HttpStatus.NO_CONTENT)
                           .body(null);
      }
   }

   @PostMapping(path = "/removeMailFileById/{numeroTramite}/{idMailFile}")
   public ResponseEntity<?> removeMailFileById(
                                    @PathVariable String numeroTramite,
                                    @PathVariable Long idMailFile,
                                    @RequestBody Usuario usrDigita) {
      
      this.mailFileService.deleteById(idMailFile);

      ExpedienteMininter expedienteMininter = this.expedienteMininterService
                                                      .findByNumeroExpedienteAndUsuarioDigita(numeroTramite, usrDigita)
                                                      .orElseThrow(DataAccessEmptyWarning::new);
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(idMailFile))
                                       .data(Arrays.asList(expedienteMininter))
                                       .build());
   }

   public static class EntitiesDto {
      public Nacionalizacion nacionalizacion;
      public ExpedienteMininter expedienteMininter;
      public DetExpedienteMininter detExpedienteMininter;
      public Usuario usuario;
      public MailFile mailFile;
   }
}
