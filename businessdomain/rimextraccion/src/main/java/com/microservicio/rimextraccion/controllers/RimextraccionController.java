package com.microservicio.rimextraccion.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.commons.utils.constants.Messages;
import com.commons.utils.controllers.CommonController;
import com.commons.utils.errors.AsignWarning;
import com.commons.utils.errors.CreateTableWarning;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.models.constants.AlterTableType;
import com.microservicio.rimextraccion.models.dto.ModuloDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.BaseDatos;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.Modulo;
import com.microservicio.rimextraccion.models.entities.QueryString;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.BaseDatosService;
import com.microservicio.rimextraccion.services.GrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.QueryStringService;
import com.microservicio.rimextraccion.services.RimextraccionService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = { "*" })
@RestController
public class RimextraccionController extends CommonController<TablaDinamica, RimextraccionService> {

   @Value("classpath:static/e_template_sim.xlsx")
   private Resource e_template_sim;

   @Autowired
   private GrupoCamposAnalisisService grupoService;

   @Autowired
   private RimasigGrupoCamposAnalisisService asigService;

   @Autowired
   private BaseDatosService baseDatosService;

   @Autowired
   private QueryStringService queryStringService;

   @Autowired
   private RimcommonService commonService;

   @PostMapping(path = { "/createTablaDinamica" })
   public ResponseEntity<?> createTablaDinamica(@RequestBody TablaDinamicaDto tablaDinamicaDto){

      String nombreTabla = tablaDinamicaDto.getNombre();
      List<TablaDinamica> tablaDinamicaDb = this.saveAndCreateTablaDinamica(tablaDinamicaDto);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_CREATE_TABLE(nombreTabla))
                  .data(tablaDinamicaDb)
                  .build());
   }

   @PutMapping(path = { "/updateNameTablaDinamica" })
   public ResponseEntity<?> updateNameTablaDinamica(@RequestBody TablaDinamica tablaDinamica) {

      String nombreTablaNew = tablaDinamica.getNombre();
      Long idTablaOld = tablaDinamica.getIdTabla();

      /* » Valida: Si, el nuevo nombre existe ... */
      Optional<TablaDinamica> tablaDinamicaNew = super.service.findByNombre(nombreTablaNew);
      if (!tablaDinamicaNew.isEmpty())
         throw new CreateTableWarning(Messages.WARNING_CREATE_TABLE(nombreTablaNew));

      /* » Obtiene: Tabla dinámica por `id` */
      TablaDinamica tablaDinamicaOld = super.service
            .findById(idTablaOld)
            .orElseThrow(() -> new CreateTableWarning(Messages.MESSAGE_WARNING_DATA_SAVE));

      /* » Actualiza: Nuevo nombre de tabla dinámica ... */
      StringBuilder queryString = new StringBuilder();
      queryString.append("SP_RENAME").append(" 'dbo.")
            .append(tablaDinamicaOld.getNombre()).append("', '")
            .append(nombreTablaNew).append("';");
      super.service.alterTablaDinamica(queryString.toString());

      /* » Actualiza: Nuevo nombre de tabla dinámica en RimTablaDinamica ... */
      tablaDinamicaOld.setNombre(nombreTablaNew);
      this.service.save(tablaDinamicaOld);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_ALTER_TABLE(nombreTablaNew))
                  .data(this.commonService.findAllTablaDinamica())
                  .build());
   }

   @DeleteMapping(path = { "/deleteTablaDinamica" })
   public ResponseEntity<?> deleteTablaDinamica(@RequestBody TablaDinamica tablaDinamica) {

      /* » Delete: Nombre en `RimTablaDinamica` ... */
      super.service.deleteById(tablaDinamica.getIdTabla());

      /* » Delete tabla dinámica ... */
      StringBuilder queryString = new StringBuilder("DROP TABLE ");
      queryString.append(tablaDinamica.getNombre());
      super.service.alterTablaDinamica(queryString.toString());

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_DELETE_TABLA(tablaDinamica.getNombre()))
                  .data(this.commonService.findAllTablaDinamica())
                  .build());
   }

   @PutMapping(path = { "/alterTablaDinamica" })
   public ResponseEntity<?> alterTablaDinamica(@RequestBody TablaDinamicaDto tablaDinamicaDto) {

      /* » Modifica: Tabla-Dinámica ... */
      this.alterMetadataOfTablaDinamica(tablaDinamicaDto);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_ALTER_TABLE(tablaDinamicaDto.getNombre()))
                  .data(super.service.findMetaTablaDinamicaByNombre(tablaDinamicaDto.getNombre()))
                  .build());
   }

   @PostMapping(path = { "/findMetaTablaDinamica" })
   public ResponseEntity<?> findMetaTablaDinamica(@RequestBody TablaDinamicaDto tablaDinamicaDto) {
      List<Map<String, Object>> metaFields = this.service.findMetaTablaDinamicaByNombre(tablaDinamicaDto.getNombre());
      if (metaFields.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(metaFields)
                  .build());
   }

   @GetMapping( path = { "/countTablaByNombre" } )
   public ResponseEntity<?> countTablaByNombre(@RequestParam String nombreTabla) {
      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(HttpStatus.OK.name())
                                 .data(this.service.countTablaByNombre(nombreTabla))
                                 .build());
   }

   @PostMapping(path = { "/saveGrupoCamposAnalisis" })
   public ResponseEntity<?> saveGrupoCamposAnalisis(@RequestBody TablaDinamicaDto tablaDinamicaDto) {

      TablaDinamica tablaDinamica = super.service
                                             .findById(tablaDinamicaDto.getIdTabla())
                                             .orElseThrow(DataAccessEmptyWarning::new);

      /* ► Nuevo: Grupo... */
      if (tablaDinamicaDto.getGrupoCamposAnalisis().getIdGrupo() == null) {

         String newGroupName = tablaDinamicaDto.getGrupoCamposAnalisis().getNombre();

         Boolean isExistingGroup = tablaDinamica
                                          .getLstGrupoCamposAnalisis()
                                          .stream()
                                          .anyMatch(g -> g.getNombre().trim().equals(newGroupName));
         if (isExistingGroup)
            throw new CreateTableWarning(Messages.WARNING_ADD_GROUP_ANALISIS(newGroupName));

         GrupoCamposAnalisis grupoCamposAnalisis = GrupoCamposAnalisis
                                                         .of()
                                                         .nombre(tablaDinamicaDto.getGrupoCamposAnalisis().getNombre())
                                                         .get();

         /* » Insertar: Grupo ... */
         tablaDinamica.addGrupoCamposAnalisis(grupoCamposAnalisis);

      /* ► Actualiza: Grupo... */
      } else {

         long idGrupo = tablaDinamicaDto.getGrupoCamposAnalisis().getIdGrupo();
         String newGroupName = tablaDinamicaDto.getGrupoCamposAnalisis().getNombre();

         tablaDinamica
                  .getLstGrupoCamposAnalisis()
                  .stream()
                  .filter(grupo -> grupo.getIdGrupo().equals(idGrupo))
                  .forEach(grupo -> {
                     grupo.setNombre(newGroupName);
                  });

         /*
          * » Inserta campo en `SimTablaDinamica` y `tmp` ...
          * → alterTableType: ADD_COLUMN_A
          * → camposCsv: sTipoControl_a TEXT, dFechaControl_a TEXT
         */
         if (tablaDinamicaDto.getCamposCsv() != null)
            this.alterMetadataOfTablaDinamica(tablaDinamicaDto);

      }

      super.service.save(tablaDinamica);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_CREATE)
                  .data(this.commonService.findAllTablaDinamica())
                  .build());
   }

   @DeleteMapping(path = { "/deleteGrupoCamposAnalisisbyId/{idGrupo}" })
   public ResponseEntity<?> deleteGrupoCamposAnalisisbyId(@PathVariable Long idGrupo){
      this.grupoService.deleteGrupoCamposAnalisisbyId(idGrupo);
      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.SUCCESS_DELETE_GRUPO_ANALISIS)
                                 .data(this.commonService.findAllTablaDinamica())
                                 .build());
   }

   @PostMapping(path = { "/uploadExtraccion" })
   public ResponseEntity<?> uploadExtraccion(@RequestParam String nombreTabla, @RequestPart MultipartFile file) throws IOException {

      /* ► Dep's ... */
      //---------------------------------------------------------------------------------------------------
      List<Map<String, Object>> metaFields = super.service.findMetaTablaDinamicaByNombre(nombreTabla);

      /* » Extrae campos de extracción y convierte a `csv` ... */
      metaFields = this.filterMetaFieldsByExtraccion(metaFields);

      /*» Campos separados por comas, para el select ...*/
      String fieldsNameCsv = this.convertMetaFieldsToCsv(metaFields);

      int totalFieldsOfTarget = metaFields.size();

      StringBuilder queryString = new StringBuilder();
      //---------------------------------------------------------------------------------------------------

      /* ► ... */
      try (XSSFWorkbook book = new XSSFWorkbook(file.getInputStream())) {
         XSSFSheet sheet = book.getSheetAt(0);

         /* » VALIDAR: Concididencia de campos de origen y destino ... */
         // ► Cantidad de columnas de la primera fila del `Resource` ...
         XSSFRow row = sheet.getRow(0);
         int totalFieldsOfSource = row.getPhysicalNumberOfCells();
         if (totalFieldsOfTarget != totalFieldsOfSource)
            throw new CreateTableWarning(Messages.WARNING_UPLOAD_TABLE(file.getOriginalFilename()));

         /* » QUERY-STRING ... */
         // ---------------------------------------------------------------------------------------------------------
         int totalRows = sheet.getPhysicalNumberOfRows();
         for (int r = 1; r < totalRows; r++) {
            row = sheet.getRow(r);/* » Fila de origen de datos ... */

            queryString.append("INSERT INTO ").append(nombreTabla)
                       .append("(").append(fieldsNameCsv).append(") VALUES(");

            /* » Itera celdas de fila ... */
            for (int c = 0; c < totalFieldsOfTarget; c++) {
               Cell cell = row.getCell(c);
               
               if (totalFieldsOfTarget == c + 1)/* » Si el cursor, lee el ultimo campo ... */
                  queryString.append(this.getCellValue(cell)).append(") ");
               else
                  queryString.append(this.getCellValue(cell)).append(", ");
            }
         }
      }

      long totalInserted = this.service.saveTablaDinamica(nombreTabla, queryString.toString());

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_BULK_FILE(totalInserted, nombreTabla))
                  .data(totalInserted)
                  .build());
   }

   @PostMapping(path = { "/assignedToGrupoAnalisis" })
   public ResponseEntity<?> assignedToGrupoAnalisis(@RequestBody(required = false) AsigGrupoCamposAnalisis asigGrupoCamposAnalisis) {

      /*► Rangos asignados ... */
      int rangeIni = asigGrupoCamposAnalisis.getRegAnalisisIni(),
          rangeFin = asigGrupoCamposAnalisis.getRegAnalisisFin();

      GrupoCamposAnalisis grupoAnalisis = this.grupoService
                                                   .findById(asigGrupoCamposAnalisis.getGrupo().getIdGrupo())
                                                   .orElseThrow(DataAccessEmptyWarning::new);

      String nombreTabla = grupoAnalisis.getTablaDinamica().getNombre();

      int totalRegExtraccion = super.service.countTablaByNombre(nombreTabla).intValue();
      if(totalRegExtraccion == 0) throw new AsignWarning(Messages.WARNING_RECORDS_NOT_FOUND_TO_SEGMENT);

      /*► Correlativo iniciado en 0 e incrementado en 1, a partir de un valor máximo ...  */
      String rangesAvailables = this.convertArrIntToStr(this.generateRangeNumbersToArr(totalRegExtraccion));

      /*► Rangos asigandos en el grupo ... */
      for (AsigGrupoCamposAnalisis asign : grupoAnalisis.getAsigGrupoCamposAnalisis()) {

         /*► Rangos asignados ...  */
         String rangosExtraccionAsign = this.convertArrIntToStr(
               this.generateRangeNumbersToArr(asign.getRegAnalisisIni(), asign.getRegAnalisisFin()));

         /*► Reemplaza `Rangos asignados` en `rangeExtraccionStr` ... */
         rangesAvailables = rangesAvailables.replace(rangosExtraccionAsign, "");
      }

      /*► Validar: Si el rango enviado está disponible ... */
      Integer[] currentRangesAvailables = this.convertStrCsvToIntArr(rangesAvailables);
      boolean isAvailableRange = Arrays
                                    .stream(this.generateRangeNumbersToArr(rangeIni, rangeFin))
                                    .allMatch(newRange -> {
                                       return Stream
                                             .of(currentRangesAvailables)
                                             .anyMatch(currentRange -> currentRange.equals(newRange));
                                    });

      /*► Save: ... */
      if (isAvailableRange) this.asigService.save(asigGrupoCamposAnalisis);
      else throw new AsignWarning(Messages.WARNING_ASIGN_REG_ANALISIS);

      /*► Response ... */
      List<TablaDinamicaDto> tablaDinamicaDb = this.commonService.findAllTablaDinamica();
      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.SUCCESS_ASIGN_REG_ANALISIS)
                                 .data(tablaDinamicaDb)
                                 .build());
   }

   @DeleteMapping(path = { "/deleteAssignedToGrupoAById/{idAsign}" })
   public ResponseEntity<?> deleteAssignedToGrupoAById(@PathVariable Long idAsign) {
      this.asigService.deleteById(idAsign);
      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(idAsign))
                  .data(this.commonService.findAllTablaDinamica())
                  .build());
   }

   @GetMapping(path = { "/findAllBasesDatos" })
   public ResponseEntity<?> findAllBasesDatos() {
      List<BaseDatos> baseDatosDb = this.baseDatosService.findAll();
      if (baseDatosDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(baseDatosDb)
                  .build());
   }

   @PostMapping(path = { "/saveQueryString" })
   public ResponseEntity<?> saveQueryString(@RequestBody ModuloDto moduloDto) {

      String nombreQueryString = moduloDto.getQueryString().getNombre();

      /* ► Validación: Si existe el nombre de Modelo de Datos → `QueryString` */
      Optional<QueryString> queryString = this.queryStringService.findByNombre(nombreQueryString);
      if (queryString.isPresent())
         throw new CreateTableWarning(Messages.WARNING_DUPLICATE_MODEL_DATA(nombreQueryString));

      QueryString queryStringNew = QueryString
                                       .of()
                                       .modulo(Modulo.of().idMod(moduloDto.getIdMod()).get())
                                       .nombre(nombreQueryString)
                                       .queryString(moduloDto.getQueryString().getQueryString())
                                       .get();

      this.queryStringService.save(queryStringNew);

      return ResponseEntity.ok().body(
            Response
                  .builder()
                  .message(Messages.SUCCESS_SAVE_DATA_MODEL(nombreQueryString))
                  .data(this.baseDatosService.findAll())
                  .build());
   }

   /* ► Client-Rest ... */
   @PostMapping(path = { "/dynamicJoinStatement" })
   public ResponseEntity<?> dynamicJoinStatement(@RequestBody TablaDinamicaDtoJoinQueryClauseDto tablaDinamicaDtoJoinQueryClauseDto) {

      TablaDinamicaDto tablaDinamicaDto = tablaDinamicaDtoJoinQueryClauseDto.tablaDinamicaDto;
      QueryClauseDto queryClauseDto = tablaDinamicaDtoJoinQueryClauseDto.queryClauseDto;
      
      String nameTable = Optional.ofNullable(queryClauseDto.getNameTable()).orElse("");

      List<Map<String, Object>> resultSet = this.service.dynamicJoinStatementSim(queryClauseDto);
      if(resultSet.size() == 0) throw new DataAccessEmptyWarning();

      if(!nameTable.isEmpty()){/*► Si recibe el `Nombre de tabla dinámica`, entonces `Insert Into in Select` ...  */

         /*►STEP-01: Regista el nombre y crea tabla dinámica ... */
         this.saveAndCreateTablaDinamica(tablaDinamicaDto);

         /*►STEP-02: Agregar campos de extracción en `Tabla dinámica` ... */
         List<String> fields = this.getKeysOfMap(resultSet.get(0))
                                                   .stream()
                                                   .map(f -> "[".concat(f).concat("_e]"))
                                                   .collect(Collectors.toList());

         StringBuilder queryStr = new StringBuilder();

         fields
            .stream()
            .map(f -> f.concat(" VARCHAR(MAX)"))
            .forEach(f -> {
               queryStr.append("ALTER TABLE ").append(nameTable)
                       .append(" ADD ").append(f).append("NULL ");
            });

         this.service.alterTablaDinamica(queryStr.toString());

         /*►STEP-03: Insertar registros a `Tabla dinámica` ... */
         String fieldsCsv = String.join(", ", fields);

         StringBuilder sqlInsertValues = new StringBuilder();
         for (Map<String, Object> fieldsMap : resultSet) {/*► Iterar `Result-Set` ... */

            String valuesCsv = fieldsMap.values()
                                             .stream()
                                             .map(Object::toString)
                                             .map(v -> v.replaceAll("'", ""))
                                             .map(v -> "'".concat(v).concat("'"))
                                             .collect(Collectors.joining(", "));

            sqlInsertValues.append("INSERT INTO ").append(nameTable).append("(")
                           .append(fieldsCsv).append(") VALUES(").append(valuesCsv)
                           .append(") ");

         }

         this.service.saveTablaDinamica(nameTable, sqlInsertValues.toString());

         return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_CREATE_TABLE(nameTable))
                                       .data(resultSet)
                                       .build());

      }

      /*► Default: Response ...  */
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(resultSet)
                                       .build());
   }

   @PostMapping(path = { "/dynamicJoinStatementDownload" })
   public ResponseEntity<?> dynamicJoinStatement2(@RequestBody QueryClauseDto queryClauseDto) throws IOException {

      /*► Fields ...  */
      String[] fieldsCsv = Arrays.stream(queryClauseDto.getFields().split(","))
                                 .map(String::trim)
                                 .map(f -> f.split("\\[")[1].replace("]", ""))
                                 .toArray(s -> new String[s]);
      
      /*► query-result:  */
      /* List<Object[]> queryResult = this.service.dynamicJoinStatementSim(queryClauseDto); */
      List<Object[]> queryResult = null;
      if(queryResult.size() == 0) {
         return ResponseEntity
                  .status(HttpStatus.NO_CONTENT)
                  .header(HttpHeaders.WARNING, Messages.MESSAGGE_WARNING_EMPTY)
                  .body(null);
      }

      /*► Depositar query-result a template `xlsx` ...  */
      try (XSSFWorkbook wb = new XSSFWorkbook(e_template_sim.getInputStream())) {

         XSSFSheet sheet = wb.getSheetAt(0);
         XSSFRow row;

         /*► Table header name ... */
         row = sheet.createRow(0);
         row.createCell(0).setCellValue("N°");
         for (int h = 0; h < fieldsCsv.length; h++) row.createCell(h + 1).setCellValue(fieldsCsv[h]);

         /*► Table body ... */
         int r = 1;
         for (Object[] record : queryResult) {
            row = sheet.createRow(r);
            int i = 0;
            for (Object item : record) {
               if(i == 0) row.createCell(i).setCellValue(r);
               row.createCell(i + 1).setCellValue(item != null ? item.toString() : "-");
               i++;
            }
            r++;
         }

         ByteArrayOutputStream op = new ByteArrayOutputStream();
         wb.write(op);

         String contentDisposition = "attachment; filename=\"%s\"";

         HttpHeaders headers = new HttpHeaders();
         headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, e_template_sim.getFilename()));

         return ResponseEntity
                  .ok()
                  .headers(headers)
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(new ByteArrayResource(op.toByteArray()));  
         
      } 
   }

   @DeleteMapping( path = { "/deleteQueryStringById/{idQStr}" } )
   public ResponseEntity<?> deleteQueryStringById(@PathVariable Long idQStr) {

      this.queryStringService.deleteById(idQStr);

      List<BaseDatos> basesDatos = this.baseDatosService.findAll();
      if(basesDatos.size() == 0) throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(basesDatos)
                                       .build());
   }

   @PutMapping( path = { "/updateQueryString" } )
   public ResponseEntity<?> updateQueryString(@RequestBody QueryString queryString) {
      
      String queryStringName = queryString.getNombre();

      Optional<QueryString> queryStringOld = this.queryStringService.findByNombre(queryStringName);
      if(queryStringOld.isPresent()) 
         throw new CreateTableWarning(Messages.WARNING_DUPLICATE_MODEL_DATA(queryStringName));

      /*► Update `RimQueryString` ... */
      QueryString queryStringNew = this.queryStringService
                                          .findById(queryString.getIdQStr())
                                          .orElseThrow(DataAccessEmptyWarning::new);

      queryStringNew.setNombre(queryStringName);

      /*► Save ...  */
      this.queryStringService.save(queryStringNew);
       
      List<BaseDatos> basesDatos = this.baseDatosService.findAll();
      if(basesDatos.size() == 0) throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_DATA_MODEL(queryStringName))
                                       .data(basesDatos)
                                       .build());
   }

   @GetMapping(path = { "/findAllTest" })
   public ResponseEntity<?> findAllTest() {
       return ResponseEntity.ok().body(DataModelHelper.convertTuplesToJson(this.service.findAllTest(), false));
   }
   
   // #region: Métodos privados ...

   private int[] generateRangeNumbersToArr(int... params) {/*► (tamaño, rangoIni, rangoFin) */

      int countParam = params.length;
      int rangoFin = 0,
          rangoIni = 1;

      switch (countParam) {
         case 1:
            rangoFin = params[0];
            break;
         case 2:
            rangoFin = (params[1] - params[0]) + 1;
            rangoIni = params[0];
            break;
      }

      int[] consecutiveArr = new int[rangoFin];
      for (int i = 0; i < rangoFin; i++) {
         consecutiveArr[i] = rangoIni + i;
      }

      return consecutiveArr;
   }

   private String convertArrIntToStr(int[] intArray) {
      String arrStr = Arrays.toString(intArray);
      return arrStr.substring(1, arrStr.length() - 1);
   }

   private Integer[] convertStrCsvToIntArr(String strCsv) {
      return Arrays
               .stream(strCsv.split(","))
               .map(String::trim)
               .filter(e -> !e.isEmpty())
               .map(Integer::parseInt)
               .toArray(Integer[]::new);
   }

   private List<String> getKeysOfMap(Map<String, Object> map){

      List<String> keys = new LinkedList<>();

      for (String key : map.keySet()) 
         keys.add(key);

      return keys;
   }

   @SuppressWarnings(value = { "deprecation" })
   private Object getCellValue(Cell cell) {
      /* ► Dep's ... */
      String fieldValue;
      
      /* ► Validación ...  */
      if(cell == null) {
         fieldValue = "-";
      } else {
         cell.setCellType(CellType.STRING);
         fieldValue = !cell.getStringCellValue().trim().isEmpty() 
                           ? cell.getStringCellValue().replaceAll("['\\/]", "")
                           : "-";
      }

      return "'".concat(fieldValue).concat("'");
   }

   private List<Map<String, Object>> filterMetaFieldsByExtraccion(List<Map<String, Object>> metaFields) {
      return metaFields
               .stream()
               .filter(field -> field.get("nombre").toString().endsWith("_e"))
               .collect(Collectors.toList());
   }

   private String convertMetaFieldsToCsv(List<Map<String, Object>> metaFields) {
      return metaFields
            .stream()
            .map(field -> field.get("nombre").toString())
            .collect(Collectors.joining(", "));
   }

   private void alterMetadataOfTablaDinamica(TablaDinamicaDto tablaDinamicaDto) {

      /* ► Dep's ...  */
      Long idTabla = tablaDinamicaDto.getIdTabla();
      String nombreTabla = tablaDinamicaDto.getNombre();
      String alterType = tablaDinamicaDto.getAlterTableType();
      String camposCsv = tablaDinamicaDto.getCamposCsv().replaceAll("[']", "").trim();

      TablaDinamica tablaDinamica = super.service
                                             .findById(idTabla)
                                             .orElseThrow(DataAccessEmptyWarning::new);

      /* » META-TABLE: Nombre y tipo ... */
      List<Map<String, Object>> metaFields = super.service.findMetaTablaDinamicaByNombre(nombreTabla);

      StringBuilder queryString = new StringBuilder();/* » Query-String ... */

      switch (alterType) {
         case AlterTableType.ADD_COLUMN_E:
         case AlterTableType.ADD_COLUMN_A:
            /* ► camposCsv: `sNumero_Pasaporte_e | TYPE | INFO` ... */
            String fieldName = camposCsv.split("\\|")[0].trim(),/* » Nombre de campo ... */
                   nameAndTypeField = fieldName.concat(" ").concat(camposCsv.split("\\|")[1].trim());

            metaFields.stream().forEach(f -> {/* » Validación: Si, nombre de campo existe en Tabla-Dinámica... */
               if (f.get("nombre").equals(fieldName))
                  throw new CreateTableWarning(Messages.WARNING_ALTER_TABLE_ADD_COLUMN(fieldName));
            });

            /* ► Inserta: Nombre de campo en tabla física ... */
            queryString
                  .append("ALTER TABLE ").append(nombreTabla)
                  .append(" ADD ").append(nameAndTypeField).append(" NULL ");

            /* ► Inserta: Nombre de campo en: `GrupoCamposAnalisis.metaFieldsCsv` ... */
            if (alterType.equals(AlterTableType.ADD_COLUMN_A)) {

               Long idGrupo = tablaDinamicaDto.getGrupoCamposAnalisis().getIdGrupo();

               tablaDinamica.getLstGrupoCamposAnalisis()
                                 .stream()
                                 .filter(g -> g.getIdGrupo().equals(idGrupo))
                                 .forEach(g -> {
                                    g.setMetaFieldsCsv(
                                          g.getMetaFieldsCsv() != null && g.getMetaFieldsCsv().trim().length() > 0
                                                ? g.getMetaFieldsCsv().concat(", ").concat(camposCsv)
                                                : camposCsv);
                                 });

            } else {

               /* ► Dep's: */
               String currentMetaFieldsCsv = Optional.ofNullable(tablaDinamica.getMetaFieldsCsv()).orElse("");

               /* ► Si `TablaDinamica.metaFieldsCsv`, no tiene iniciallizada almenos 1 campo de `Extracción` ... */
               if(currentMetaFieldsCsv.isEmpty())
                  tablaDinamica.setMetaFieldsCsv(camposCsv);
               else
                  tablaDinamica.setMetaFieldsCsv(currentMetaFieldsCsv.concat(", ").concat(camposCsv));

            }

            this.service.save(tablaDinamica);

            break;
         case AlterTableType.ALTER_COLUMN_E:
         case AlterTableType.ALTER_COLUMN_A:

            /* ► camposCsv: `sNumero_Pasaporte_e | TYPE | INFO` ... */
            String prevMetaField = camposCsv.split(",")[0].trim(),
                   nextMetaField = camposCsv.split(",")[1].trim(),
                   prevFieldName = prevMetaField.split("\\|")[0].trim(),
                   nextFieldName = nextMetaField.split("\\|")[0].trim(),
                   nameAndTypeFromNextField = "[".concat(nextFieldName).concat("] ")
                                                .concat(nextMetaField.split("\\|")[1].trim());

            /* » Actualizar nombre de campo en tabla física ... */
            queryString.append("SP_RENAME").append(" 'dbo.")
                       .append(nombreTabla).append(".[").append(prevFieldName)
                       .append("]', '").append(nextFieldName).append("', 'COLUMN' ");

            /* » Actualizar meta-datos de campo en tabla física ... */
            queryString.append("ALTER TABLE ").append(nombreTabla)
                       .append(" ALTER COLUMN ").append(nameAndTypeFromNextField);

            /* ► Si el Campo es: `_a` ... */
            if (alterType.equals(AlterTableType.ALTER_COLUMN_A)) {
               Long idGrupo = tablaDinamicaDto.getGrupoCamposAnalisis().getIdGrupo();

               tablaDinamica
                     .getLstGrupoCamposAnalisis()
                     .stream()
                     .filter(g -> g.getIdGrupo().equals(idGrupo))
                     .forEach(g -> {
                        g.setMetaFieldsCsv(g.getMetaFieldsCsv().replace(prevMetaField, nextMetaField));
                     });
            } else { /* ► Si el Campo es: `_e` ... */

               /* ► Dep's: */
               String currentMetaFieldsCsv = Optional.ofNullable(tablaDinamica.getMetaFieldsCsv()).orElse("");

               /* ► Si `TablaDinamica.metaFieldsCsv`, no tiene iniciallizada almenos 1 campo de `Extracción` ... */
               if(currentMetaFieldsCsv.isEmpty()){
                  tablaDinamica.setMetaFieldsCsv(nextMetaField);
                  
               } else {/* ► Si `TablaDinamica.metaFieldsCsv`, tiene iniciallizada almenos 1 campo de `Extracción` ... */

                  /* ► Si existe `prevMetaField` en `currentMetaFieldsCsv` ... */
                  if(currentMetaFieldsCsv.contains(prevMetaField))
                     tablaDinamica.setMetaFieldsCsv(currentMetaFieldsCsv.replace(prevMetaField, nextMetaField));
                  else
                     tablaDinamica.setMetaFieldsCsv(currentMetaFieldsCsv.concat(", ").concat(nextMetaField));

               }

            }

            /* ► Save ... */
            super.service.save(tablaDinamica);

            break;
         case AlterTableType.DROP_COLUMN_E:
         case AlterTableType.DROP_COLUMN_A:

            /*► Dep's ... */
            fieldName = camposCsv.split(" ")[0].toString().trim();

            /*► Elimina: Campo físico ... */
            queryString
                  .append("ALTER TABLE ").append(nombreTabla)
                  .append(" DROP COLUMN [").append(fieldName).append("]");
            
            /*► Si `alterType` es `_e` */
            if(alterType.equals(AlterTableType.DROP_COLUMN_E)){

               /*► Dep's  */
               String currentMetaFieldsCsv = Optional.ofNullable(tablaDinamica.getMetaFieldsCsv()).orElse("");
               
               /*► Si `TablaDinamica.metaFieldsCsv` está vacia, entonces fué extraida `SIM` ... */
               if(!currentMetaFieldsCsv.isEmpty()){
                  
                  String newMetaFieldsCsv = Arrays.stream(currentMetaFieldsCsv.split(","))
                                                  .filter(mf -> !mf.contains(fieldName))
                                                  .map(String::trim)
                                                  .collect(Collectors.joining(","));

                  tablaDinamica.setMetaFieldsCsv(newMetaFieldsCsv);

                  /*► Save ...  */
                  this.service.save(tablaDinamica);
               }
               
            } else {/*► Si `alterType` es `_a` ... */

               /*► Dep's  */
               Long idGrupoForRemoval = tablaDinamicaDto.getGrupoCamposAnalisis().getIdGrupo();
               String currentMetaFieldsCsv = tablaDinamica.getLstGrupoCamposAnalisis()
                                                          .stream()
                                                          .filter(g -> g.getIdGrupo().equals(idGrupoForRemoval))
                                                          .map(g -> g.getMetaFieldsCsv())
                                                          .findFirst()
                                                          .orElse("");
               
               if(!currentMetaFieldsCsv.isEmpty()){
                  
                  String newMetaFieldsCsv = Arrays.stream(currentMetaFieldsCsv.split(","))
                                                  .filter(mf -> !mf.contains(fieldName))
                                                  .map(String::trim)
                                                  .collect(Collectors.joining(","));

                  tablaDinamica
                        .getLstGrupoCamposAnalisis()
                        .stream()
                        .filter(g -> g.getIdGrupo().equals(idGrupoForRemoval))
                        .forEach(g -> {
                           g.setMetaFieldsCsv(newMetaFieldsCsv);
                        });

                  /*► Save ...  */
                  this.service.save(tablaDinamica);
               }
            }

            break;
      }

      /* » Alter table ... */
      super.service.alterTablaDinamica(queryString.toString());
   }

   private List<TablaDinamica> saveAndCreateTablaDinamica(TablaDinamicaDto tablaDinamicaDto){

      String nombreTabla = tablaDinamicaDto.getNombre();

      /* » Valida: Si, existe tabla dinámica... */
      Optional<TablaDinamica> tablaDinamicaOld = super.service.findByNombre(nombreTabla);
      if (tablaDinamicaOld.isPresent())
         throw new CreateTableWarning(Messages.WARNING_CREATE_TABLE(nombreTabla));

      /* » Insert: Nombre de tabla ... */
      TablaDinamica tablaDinamicaNew = new ModelMapper().map(tablaDinamicaDto, TablaDinamica.class);
      this.service.save(tablaDinamicaNew);

      /* » Create: tabla dinámica física ... */
      super.service.createTable(nombreTabla);

      return this.service.findAll();
   }

   // #endregion

   // #region Body-Request ...

   public static class TablaDinamicaDtoJoinQueryClauseDto {
      public TablaDinamicaDto tablaDinamicaDto;
      public QueryClauseDto queryClauseDto;
   }

   // #endregion

}