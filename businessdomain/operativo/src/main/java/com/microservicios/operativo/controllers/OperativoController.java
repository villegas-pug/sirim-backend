package com.microservicios.operativo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import com.commons.utils.constants.Calendario;
import com.commons.utils.constants.Messages;
import com.commons.utils.controllers.CommonController;
import com.commons.utils.errors.EntityFindByIdWarning;
import com.commons.utils.models.dto.FilterOpeDto;
import com.commons.utils.models.dto.RptOperativoDto;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.entities.DetalleOperativo;
import com.microservicios.operativo.models.entities.Operativo;
import com.microservicios.operativo.models.entities.OperativoJZ;
import com.microservicios.operativo.services.OperativoJZService;
import com.microservicios.operativo.services.OperativoService;
import org.apache.poi.ss.usermodel.CellType;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "*" }, exposedHeaders = { "*" })
@RestController
public class OperativoController extends CommonController<Operativo, OperativoService> {

   @Autowired
   private OperativoJZService operativoJZService;

   @Value("classpath:static/S08-DGTFM-FR001 Reporte Operativos VFM_V01.xlsx")
   private Resource rptS08DGTFMFR001;

   /* @HystrixCommand(fallbackMethod = "alternateSave") */
   @PostMapping(path = "/save", produces = { MediaType.APPLICATION_JSON_VALUE })
   public ResponseEntity<?> save(@RequestPart Operativo operativo, @RequestPart(required = false) MultipartFile file) {
      /* » Validar: xls-file ... */
      if (file != null)
         operativo.setDetOperativo(this.convertObjXlsxToList(file));

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_CREATE())
                  .data(Arrays.asList(super.service.save(operativo)))
                  .build());
   }

   @GetMapping(path = "/countPivotedByOpeAnual")
   public ResponseEntity<?> countPivotedByOpeAnual() {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedByOpeAnual())
                  .build());
   }

   @GetMapping(path = "/countPivotedByIntervenidos")
   public ResponseEntity<?> countPivotedByIntervenidos(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String sexo,
         @RequestParam(defaultValue = "") String dependencia) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedByIntervenidos(añoOpe, sexo, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedByTipoInfraccion")
   public ResponseEntity<?> countPivotedByTipoInfraccion(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String sexo,
         @RequestParam(defaultValue = "") String dependencia) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedByTipoInfraccion(añoOpe, sexo, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedByTipoOperativo")
   public ResponseEntity<?> countPivotedByTipoOperativo(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String sexo,
         @RequestParam(defaultValue = "") String dependencia) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedByTipoOperativo(añoOpe, sexo, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedOpeByNacionalidad")
   public ResponseEntity<?> countPivotedOpeByNacionalidad(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String sexo,
         @RequestParam(defaultValue = "") String dependencia) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(super.service.countPivotedOpeByNacionalidad(añoOpe, sexo, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedOpeBySexo")
   public ResponseEntity<?> countPivotedOpeBySexo(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String dependencia) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedBySexo(añoOpe, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedOpeByModalidad")
   public ResponseEntity<?> countPivotedOpeByModalidad(
         @RequestParam(defaultValue = "") String añoOpe,
         @RequestParam(defaultValue = "") String dependencia) {

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(super.service.countPivotedOpeByModalidad(añoOpe, dependencia))
                  .build());
   }

   @GetMapping(path = "/countPivotedOpe")
   public ResponseEntity<?> countPivotedOpe() {

      List<Map<String, Object>> opePivotedDb = new ArrayList<>();
      super.service.countPivotedOpe().stream().forEach(recordDb -> {
         Map<String, Object> record = new HashMap<>();
         record.put("monthOpe", Calendario.namesOfMonths[(int) ((Object[]) recordDb)[0] - 1]);
         record.put("2019", ((Object[]) recordDb)[1]);
         record.put("2020", ((Object[]) recordDb)[2]);
         record.put("2021", ((Object[]) recordDb)[3]);
         opePivotedDb.add(record);
      });

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(opePivotedDb)
                  .build());
   }

   @PostMapping(path = "/findOpeByCustomFilterToExcel")
   public ResponseEntity<?> findOpeByCustomFilterToExcel(@RequestBody FilterOpeDto filterOpeDto) throws IOException {
      List<RptOperativoDto> operativoDb = super.service.findOpeByCustomFilterToExcel(
            filterOpeDto.getIdOpe(),
            filterOpeDto.getFecIni(),
            filterOpeDto.getFecFin(),
            filterOpeDto.getDependencia().getNombre(),
            filterOpeDto.getModalidad(),
            filterOpeDto.getSexo(),
            filterOpeDto.getTipoOperativo().getDescripcion());

      if (operativoDb.size() == 0)
         return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .body(null);

      try (XSSFWorkbook book = new XSSFWorkbook(rptS08DGTFMFR001.getInputStream())) {

         SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
         int firstRowIndex = 7;

         XSSFSheet sheetIntervenidos = book.getSheetAt(1);/*-> sheet: `Intervenidos` */
         int i = 0;
         for (RptOperativoDto intervenido : operativoDb) {
            i++;
            XSSFRow rowIntervenido = sheetIntervenidos.getRow(firstRowIndex + i - 1);
            rowIntervenido.getCell(0).setCellValue(i);
            rowIntervenido.getCell(1).setCellValue(intervenido.getModalidadOperativo());
            rowIntervenido.getCell(2).setCellValue(intervenido.getTipoOperativo());
            rowIntervenido.getCell(3).setCellValue(dateFormat.format(intervenido.getFechaOperativo()));
            rowIntervenido.getCell(4).setCellValue(intervenido.getNombres());
            rowIntervenido.getCell(5).setCellValue(intervenido.getExibicionDocIdentidadOViaje());
            rowIntervenido.getCell(6).setCellValue(intervenido.getTipoDocumento());
            rowIntervenido.getCell(7).setCellValue(intervenido.getNumeroDocumento());
            rowIntervenido.getCell(8).setCellValue(intervenido.getNacionalidad());
            rowIntervenido.getCell(9).setCellValue(intervenido.getSexo());
            rowIntervenido.getCell(10).setCellValue(dateFormat.format(intervenido.getFechaNacimiento()));
            rowIntervenido.getCell(11).setCellValue(intervenido.getEdad());
            rowIntervenido.getCell(12).setCellValue(intervenido.getInfraccion());
            rowIntervenido.getCell(13).setCellValue(intervenido.getTipoInfraccion());
            rowIntervenido.getCell(14).setCellValue(intervenido.getDisposicionPNP());
            rowIntervenido.getCell(15).setCellValue(intervenido.getSituacionMigratoria());
            rowIntervenido.getCell(16).setCellValue(intervenido.getRefugiado());
            rowIntervenido.getCell(17).setCellValue(intervenido.getAlertaMigratoria());
            rowIntervenido.getCell(18).setCellValue(intervenido.getObservaciones());
         }

         /* » */
         XSSFSheet sheetInfraccion = book.getSheetAt(2);/*-> sheet: `Infracción` */
         i = 0;
         List<RptOperativoDto> opeInfraccionDb = operativoDb.stream()
               .filter(record -> record.getInfraccion().equalsIgnoreCase("SI"))
               .collect(Collectors.toList());

         for (RptOperativoDto intervenido : opeInfraccionDb) {
            XSSFRow rowInfraccion = sheetInfraccion.getRow(firstRowIndex + i);
            rowInfraccion.getCell(0).setCellValue(i + 1);
            rowInfraccion.getCell(1).setCellValue(intervenido.getModalidadOperativo());
            rowInfraccion.getCell(2).setCellValue(intervenido.getTipoOperativo());
            rowInfraccion.getCell(3).setCellValue(dateFormat.format(intervenido.getFechaOperativo()));
            rowInfraccion.getCell(4).setCellValue(intervenido.getNombres());
            rowInfraccion.getCell(5).setCellValue(intervenido.getExibicionDocIdentidadOViaje());
            rowInfraccion.getCell(6).setCellValue(intervenido.getTipoDocumento());
            rowInfraccion.getCell(7).setCellValue(intervenido.getNumeroDocumento());
            rowInfraccion.getCell(8).setCellValue(intervenido.getNacionalidad());
            rowInfraccion.getCell(9).setCellValue(intervenido.getSexo());
            rowInfraccion.getCell(10).setCellValue(dateFormat.format(intervenido.getFechaNacimiento()));
            rowInfraccion.getCell(11).setCellValue(intervenido.getEdad());
            rowInfraccion.getCell(12).setCellValue(intervenido.getInfraccion());
            rowInfraccion.getCell(13).setCellValue(intervenido.getTipoInfraccion());
            rowInfraccion.getCell(14).setCellValue(intervenido.getDisposicionPNP());
            rowInfraccion.getCell(15).setCellValue(intervenido.getSituacionMigratoria());
            rowInfraccion.getCell(16).setCellValue(intervenido.getRefugiado());
            rowInfraccion.getCell(17).setCellValue(intervenido.getAlertaMigratoria());
            rowInfraccion.getCell(18).setCellValue(intervenido.getObservaciones());
            i++;
         }

         ByteArrayOutputStream output = new ByteArrayOutputStream();
         book.write(output);

         String contentDisposition = "attachment; filename=\"%s.xlsx\"";
         HttpHeaders header = new HttpHeaders();
         header.add(
               HttpHeaders.CONTENT_DISPOSITION,
               String.format(contentDisposition, rptS08DGTFMFR001.getFilename().split(".xlsx")[0]));

         return ResponseEntity
               .ok()
               .headers(header)
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .body(new ByteArrayResource(output.toByteArray()));
      } catch (Exception e) {
         return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .body(null);
      }
   }

   @PutMapping(path = "/updateOpeById/{idOpe}/{numeroInforme}")
   public ResponseEntity<?> updateOpeById(@PathVariable(value = "") long idOpe, @PathVariable String numeroInforme) {
      Operativo opeDb = super.service.findById(idOpe)
            .orElseThrow(() -> new EntityFindByIdWarning(idOpe));

      opeDb.setNumeroInforme(numeroInforme);
      super.service.save(opeDb);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_UPDATE)
                  .data(super.service.findAll())
                  .build());
   }

   private List<DetalleOperativo> convertObjXlsxToList(MultipartFile file) {
      try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
         XSSFSheet sheet = workbook.getSheetAt(0);/* » Sheet: Template... */
         List<DetalleOperativo> detOperativoDb = new ArrayList<>();

         int firstRowIndex = 7;
         for (int i = firstRowIndex; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(1).getStringCellValue().isEmpty())
               break;

            /* » Casting cell type to string ... */
            row.getCell(4).setCellType(CellType.STRING);

            detOperativoDb.add(new DetalleOperativo(
                  row.getCell(1).getStringCellValue(),
                  row.getCell(2).getStringCellValue(),
                  row.getCell(3).getStringCellValue(),
                  row.getCell(4).getStringCellValue(),
                  super.service.findByNacionalidad(row.getCell(5).getStringCellValue()).get(),
                  row.getCell(6).getStringCellValue(),
                  row.getCell(7).getDateCellValue(), // » fecNac ...
                  row.getCell(8).getStringCellValue(),
                  row.getCell(9).getStringCellValue(),
                  row.getCell(10).getStringCellValue(),
                  row.getCell(11).getStringCellValue(),
                  row.getCell(12).getStringCellValue(),
                  row.getCell(13).getStringCellValue(),
                  row.getCell(14).getStringCellValue()));
         }
         return detOperativoDb;
      } catch (IOException e) {
         return null;
      }
   }

   @GetMapping(path = "/findAllOpeJZ")
   public ResponseEntity<?> findAllOpeJZ() {
      List<OperativoJZ> opeJZDb = this.operativoJZService.findAll();
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(opeJZDb)
                  .build());
   }

   /* » Hystric fallback method... */
   public ResponseEntity<?> alternateSave(@RequestPart Operativo operativo, @RequestPart MultipartFile file) {
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_CREATE)
                  .data(Arrays.asList())
                  .build());
   }

   @PostMapping(path = { "/createTable" })
   public ResponseEntity<?> createTable() {
      List<Tuple> tuple = this.service.createTable();

      List<Tuple> response = new ArrayList<>();

      List<Map<String, Object>> list = new ArrayList<>();

      for (Tuple row : tuple) {
         response.add(row);
         Map<String, Object> map = new HashMap<>();
         ;

         for (TupleElement<?> element : row.getElements()) {
            String alias = element.getAlias();
            map.put(alias, row.get(alias));
         }
         list.add(map);
      }

      List<Object> test = new ArrayList<>();
      test.add("Name");
      test.add(5);

      return ResponseEntity.ok(tuple);
   }

}