package com.microservicio.rimextraccion.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.models.enums.RimGrupo;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.clients.UsuarioClientRest;
import com.microservicio.rimextraccion.errors.NotFoundDownloadException;
import com.microservicio.rimextraccion.errors.RimcommonWarningException;
import com.microservicio.rimextraccion.helpers.RimanalisisPoiHelper;
import com.microservicio.rimextraccion.helpers.RimcommonHelper;
import com.microservicio.rimextraccion.helpers.RimanalisisPoiHelper.CellType;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.dto.RecordsBetweenDatesDto;
import com.microservicio.rimextraccion.models.dto.ReporteMensualProduccionDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import com.microservicio.rimextraccion.repository.RimproduccionRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RimanalisisServiceImpl implements RimanalisisService {

   @Value("classpath:static/S10.DRCM.FR.001-Registro de depuracion de datos_V02.xlsx")
   private Resource rptProduccionDiarioXlsx;

   @Value("classpath:static/Ficha de reporte de produccion v1.0.xlsx")
   private Resource rptProduccionMensualXlsx;

   @Autowired
   private RimproduccionRepository rimproduccionRepository;

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired
   private RimextraccionService rimextraccionService;

   @Autowired 
   private RimasigGrupoCamposAnalisisService rimasigAnalisisService;

   @Autowired
   private UsuarioClientRest usuarioClient;

   //#region Repo - Method's ...

   @Override
   @Transactional
   public void saveRecordAssigned(RecordAssignedDto recordAssignedDto) {
      
      // ► Dep's: Convert `JSON` to `CSV` ...
      String cleanJsonValues = recordAssignedDto.getValues().replaceAll("[{}]", ""),
             csvFields = Arrays.stream(cleanJsonValues.split(","))
                                 .map(i -> { 
                                    String[] field = i.split(":");
                                    return 
                                       field[0].replaceAll("[\"\']", "")
                                               .concat("=")
                                               .concat(field[1].replaceAll("\"", "'"));
                                 })
                                 .collect(Collectors.joining(", "));
         
      // ► Query-String ...
      StringBuilder queryString = new StringBuilder("UPDATE ");
      queryString
         .append(recordAssignedDto.getNombreTable()).append(" SET ").append(csvFields)
         .append(" WHERE nId = ").append(recordAssignedDto.getId());

      // ► Save: Registro en tabla físisca ...
      this.rimextraccionService.alterTablaDinamica(queryString.toString());

      // ► Grupo asignado ...
      /*-------------------------------------------------------------------------------------------------------*/
      Long idAsigGrupo = recordAssignedDto.getAsigGrupo().getIdAsigGrupo();
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigAnalisisService.findById(idAsigGrupo);

      // ► Si producción está registrada ...
      Long idRegistro = recordAssignedDto.getId();
      boolean isRegistered = asigGrupoCamposAnalisis
                                 .getProduccionAnalisis()
                                 .stream()
                                 .anyMatch(prod -> prod.getIdRegistroAnalisis().equals(idRegistro));

      if (isRegistered) {
         asigGrupoCamposAnalisis.getProduccionAnalisis()
                                    .stream()
                                    .filter(prod -> prod.getIdRegistroAnalisis().equals(idRegistro))
                                    .forEach(prod -> prod.setFechaFin(new Date()));
      } else {
         asigGrupoCamposAnalisis.addProduccionAnalisis(ProduccionAnalisis.of().idRegistroAnalisis(idRegistro).get());
      }

      /*► Save ... */
      this.rimasigAnalisisService.save(asigGrupoCamposAnalisis);

   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString) {
      List<Map<String, Object>> recordsAnalisados = this.rimcommonService.findDynamicSelectStatement(queryString);
      return recordsAnalisados;
   }

   @Override
   @Transactional(readOnly = true)
   public LinkedList<ReporteMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year) {
      return this.rimproduccionRepository.findReporteMensualProduccionByParams(idUsrAnalista, month, year);
   }

   //#endregion

   //#region Client method's ...

   @Override
   @Transactional(readOnly = true)
   public Response<Usuario> findUsuarioByLogin(String login) {
      return Optional
               .ofNullable(this.usuarioClient.findByLogin(login))
               .orElseThrow(() -> new RimcommonWarningException(Messages.WARNING_USER_NOT_EXISTS));
   }

   //#endregion

   //#region: Custom method's ...

   @Override
   public ByteArrayResource convertProduccionAnalisisToByteArrResource(RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException{

      /*► GLOBAL - DEP'S ... */
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigAnalisisService.findById(recordsBetweenDatesDto.getIdAsigGrupo());
      if(asigGrupoCamposAnalisis == null)
         throw new NotFoundDownloadException();

      RimGrupo usrGrupo = asigGrupoCamposAnalisis.getUsrAnalista().getGrupo();
      String nombreTabla = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getNombre(),
             usrAnalista = asigGrupoCamposAnalisis.getUsrAnalista().getNombres(),
             usrAnalistaCargo = asigGrupoCamposAnalisis.getUsrAnalista().getCargo(),
             usrCreador = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getUsrCreador().getNombres(),
             usrCreadorCargo = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getUsrCreador().getCargo(),
             metaNameAssignedCsv = RimcommonHelper.convertMetaFieldsCsvToFieldsNameCsv( asigGrupoCamposAnalisis.getGrupo().getMetaFieldsCsv()),
             metafieldsACsv = asigGrupoCamposAnalisis.getGrupo().getMetaFieldsCsv(),
             metafieldsECsv = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getMetaFieldsCsv(),
             idsProdCsv = asigGrupoCamposAnalisis
                              .getProduccionAnalisis()
                              .stream()
                              .filter(prod -> prod.getFechaFin().compareTo(recordsBetweenDatesDto.getFecIni()) >= 0 
                                              && prod.getFechaFin().compareTo(recordsBetweenDatesDto.getFecFin()) <= 0 )
                              .map(prod -> prod.getIdRegistroAnalisis().toString())
                              .collect(Collectors.joining(","));
      
      /*► Validación: Si no hay id's analizados ... */
      if(idsProdCsv.isEmpty()) throw new NotFoundDownloadException();
                              
      /*► Query-String ... */
      StringBuilder queryString = new StringBuilder();
      queryString.append("SELECT * FROM ").append(nombreTabla)
                 .append(" WHERE nId IN (")
                 .append(idsProdCsv).append(")");

      /*► Data-Set ... */
      //------------------------------------------------------------------------------------------------------
      List<Map<String, Object>> ds = this.rimcommonService.findDynamicSelectStatement(queryString.toString());
      if(ds.size() == 0) throw new NotFoundDownloadException();

      List<Map<String, Object>> dsNew = this.purgeProduccionAnalisisDb(ds, metaNameAssignedCsv);
      //------------------------------------------------------------------------------------------------------

      ByteArrayOutputStream op = new ByteArrayOutputStream();

      try (XSSFWorkbook wb = new XSSFWorkbook(rptProduccionDiarioXlsx.getInputStream())) {

         XSSFSheet ws = wb.getSheetAt(0);
         
         /*► GLOBAL-DEP'S ... */
         int rowIndexHeader = 10;
         int columnWidth = 6000;
         Cell dynamicCell;

         /*► HEADER: Tag's Static's ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's: ... */
         XSSFRow rowCodigo = ws.getRow(3);
         rowCodigo.getCell(1).setCellValue(
                                          usrGrupo == RimGrupo.DEPURACION
                                             ? "S10.DRCM.FR.001"
                                             : "S10.DRCM.FR.002");

         XSSFRow rowVersion = ws.getRow(3);
         rowVersion.getCell(4).setCellValue(usrGrupo == RimGrupo.DEPURACION ? "'01" : "'02");

         XSSFRow rowServidorCargo = ws.getRow(5);
         Cell cellServidorCargo = rowServidorCargo.getCell(5);
         cellServidorCargo.setCellValue(usrAnalistaCargo);

         XSSFRow rowServidorRemite = ws.getRow(6);
         Cell cellServidorRemite = rowServidorRemite.getCell(5);
         cellServidorRemite.setCellValue(usrAnalista);

         XSSFRow rowNombreBase = ws.getRow(7);
         Cell cellNombreBase = rowNombreBase.getCell(5);
         cellNombreBase.setCellValue(nombreTabla);
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► HEADER-TABLE: Extracción y Analisis ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         XSSFRow rowHeader = ws.createRow(rowIndexHeader);
         rowHeader.setHeightInPoints((short) 79.5);
         int iCellHeader = 0;

         /*► Static-Cell's ...  */
         iCellHeader++;
         Cell cellNro = rowHeader.createCell(iCellHeader);
         cellNro.setCellValue("N°");
         cellNro.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_EXTRACCION));
         ws.setColumnWidth(iCellHeader, 2500);

         iCellHeader++;
         Cell cellId = rowHeader.createCell(iCellHeader);
         cellId.setCellValue("ID");
         cellId.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_EXTRACCION));
         ws.setColumnWidth(iCellHeader, 2500);

         /*► Dynamic-row's ...  */
         int iCellA = 0;

         for (Entry<String, Object> record : dsNew.get(0).entrySet()) {
            if(record.getKey().endsWith("_e")){
               iCellHeader++;
               dynamicCell = rowHeader.createCell(iCellHeader);
               dynamicCell.setCellValue(RimcommonHelper.undecoratedMetaFieldName(record.getKey()));
               dynamicCell.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_EXTRACCION));
               ws.setColumnWidth(iCellHeader, 5000);
            }

            if(record.getKey().endsWith("_a")){
               iCellHeader++;
               iCellA++;
               dynamicCell = rowHeader.createCell(iCellHeader);
               StringBuilder cellValueA = new StringBuilder();
               cellValueA.append("Criterio de análisis \n").append(String.valueOf(iCellA))
                         .append("- ").append(RimcommonHelper.undecoratedMetaFieldName(record.getKey()));
               dynamicCell.setCellValue(cellValueA.toString());
               dynamicCell.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_ANALISIS));
               ws.setColumnWidth(iCellHeader, columnWidth);
            }
         }
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► HEADER-TABLE: Control de Calidad ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Static-Cell's ...  */
         iCellHeader++;
         Cell cellCtrlCal = rowHeader.createCell(iCellHeader);
         cellCtrlCal.setCellValue("Control de calidad");
         cellCtrlCal.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_CONTROL_CALIDAD));
         ws.setColumnWidth(iCellHeader, columnWidth);

         iCellHeader++;
         Cell cellCriteriosErr = rowHeader.createCell(iCellHeader);
         cellCriteriosErr.setCellValue("N° de criterios errados");
         cellCriteriosErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_CONTROL_CALIDAD));
         ws.setColumnWidth(iCellHeader, columnWidth);

         iCellHeader++;
         Cell cellDetalleErr = rowHeader.createCell(iCellHeader);
         cellDetalleErr.setCellValue("Detalle del error");
         cellDetalleErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_CONTROL_CALIDAD));
         ws.setColumnWidth(iCellHeader, columnWidth);

         iCellHeader++;
         Cell cellPorcErr = rowHeader.createCell(iCellHeader);
         cellPorcErr.setCellValue("% de error");
         cellPorcErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_CONTROL_CALIDAD));
         ws.setColumnWidth(iCellHeader, columnWidth);
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► HEADER-TABLE: Info ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         XSSFRow rowInfo = ws.createRow(rowIndexHeader + 1);
         rowInfo.setHeightInPoints((short) 68.25);
         int iCellInfo = 0;

         /*► Static-cell: Chunk-01 ... */
         iCellInfo++;
         cellNro = rowInfo.createCell(iCellInfo);
         cellNro.setCellValue("Colocar ítem");
         cellNro.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));

         iCellInfo++;
         cellId = rowInfo.createCell(iCellInfo);
         cellId.setCellValue("Colocar el código de identificación del registro en la base de datos");
         cellId.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));

         /*► Dynamic-cell's ... */
         Map<String, String> metaInfoMap = RimcommonHelper.convertMetaFieldsEAndACsvToMetaInfoMap(metafieldsECsv, metafieldsACsv);
         for (Entry<String, Object> record : dsNew.get(0).entrySet()) {
            if(record.getKey().endsWith("_e") || record.getKey().endsWith("_a")){
               iCellInfo++;
               dynamicCell = rowInfo.createCell(iCellInfo);
               dynamicCell.setCellValue(metaInfoMap.get(record.getKey()));
               dynamicCell.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));
            }
         }

         /*► Static-cell: Chunk-02 ... */
         iCellInfo++;
         cellCtrlCal = rowInfo.createCell(iCellInfo);
         cellCtrlCal.setCellValue("Colocar \"SI\"  en el caso que el regitro sea parte de la muestra de control de calidad, caso contrario consignar \"NO\" ");
         cellCtrlCal.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));

         iCellInfo++;
         cellCriteriosErr = rowInfo.createCell(iCellInfo);
         cellCriteriosErr.setCellValue("Colocar la cantidad de criterios errados.");
         cellCriteriosErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));
         
         iCellInfo++;
         cellDetalleErr = rowInfo.createCell(iCellInfo);
         cellDetalleErr.setCellValue("Especificar el/los criterio(s) incorrecto(s)");
         cellDetalleErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));

         iCellInfo++;
         cellPorcErr = rowInfo.createCell(iCellInfo);
         cellPorcErr.setCellValue("Colocar el % del error en función al número de criterios de análisis");
         cellPorcErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO));
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► BODY: Registros de `Extracción`, `Analisis` y `Control Calidad` ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         // Dep's ...
         int iR = 0,
             iI = 0,
             iRowAnalisis = 11;
         
         for (Map<String, Object> record : dsNew) {
            iR++;
            XSSFRow rowAnalisis = ws.createRow(iRowAnalisis + iR);
            
            // N°
            iI++;
            Cell cellAnalisis = rowAnalisis.createCell(iI);
            cellAnalisis.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));
            cellAnalisis.setCellValue(String.valueOf(iR));

            /*► Dynamic-Cell: `E` y `A` ... */
            for (Entry<String, Object> item : record.entrySet()) {
               iI++;
               cellAnalisis = rowAnalisis.createCell(iI);
               cellAnalisis.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));
               cellAnalisis.setCellValue(Optional.ofNullable(item.getValue()).orElse("-").toString());
            }

            /*► Static-Cell: `Control de Calidad` ... */
            for (int sc = 1; sc <= 4; sc++) {
               iI++;
               cellAnalisis = rowAnalisis.createCell(iI);
               cellAnalisis.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));
               cellAnalisis.setCellValue("-");
            }

            // Cleanup ...
            iI = 0;
         }

         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► FOOTER: Obs ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         int iRowFooter = ws.getPhysicalNumberOfRows() + 2;
         XSSFRow rowObs = ws.createRow(iRowFooter);
         rowObs.setHeightInPoints((short) 78.75);
         
         /*► Static-cell's ... */
         Cell cellObs = rowObs.createCell(1);
         cellObs.setCellValue("OBSERVACIONES:\n- ");
         cellObs.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.FOOTER_CELL_OBS));
         ws.addMergedRegion(new CellRangeAddress(iRowFooter, iRowFooter, 1, iCellHeader));
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► FOOTER: Leyenda ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         int iRowLeyenda = ws.getPhysicalNumberOfRows() + 3;

         XSSFRow rowLeyenda = ws.createRow(iRowLeyenda);
         XSSFRow rowLeyendaDet1 = ws.createRow(iRowLeyenda + 1);
         XSSFRow rowLeyendaDet2 = ws.createRow(iRowLeyenda + 2);

         rowLeyendaDet2.setHeightInPoints((short) 35);

         /*► Static-cell's ... */
         Cell cellLeyenda = rowLeyenda.createCell(1);
         cellLeyenda.setCellValue("Leyenda:");

         Cell cellLeyendaDet1 = rowLeyendaDet1.createCell(1);
         cellLeyendaDet1.setCellValue("Información adicional: Se puede considerar información como \"Calidad migratoria\", \"Último movimiento migratorio\", \"Tipo de trámite\", \"Número de trámite\", \"Edad\", \"Estado civil\", entre otros que se requieran para el análisis.");
         ws.addMergedRegion(new CellRangeAddress(iRowLeyenda + 1, iRowLeyenda + 1, 1, iCellHeader));

         Cell cellLeyendaDet2 = rowLeyendaDet2.createCell(1);
         cellLeyendaDet2.setCellValue("Criterios de análisis: dentro de los criterios se puede considerar \"Cantidad de Registros RCM\", \"Documentos asociados\", \"Otra nacionalidad\", \"Documento de viaje incorrecto\", \"Menor de 12 años\", \"Vencimiento correcto\", \"Fecha de vencimiento\", \"Estado del pasaporte\", \"Pasaporte vigente\", \"Vinculación con terceros\", \"Tipo y número de documento de vinculación\", \"Registro de entradas consecutivas\", \"Registro de salidas consecutivas\", entre otros que permitan identificar la información a ser validada.");
         cellLeyendaDet2.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.FOOTER_CELL_LEYENDA_2));
         ws.addMergedRegion(new CellRangeAddress(iRowLeyenda + 2, iRowLeyenda + 2, 1, iCellHeader));
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► FOOTER: CopyRight ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         int iRowFooterCopyRight = ws.getPhysicalNumberOfRows() + 4;
         XSSFRow rowFooterCopyRight = ws.createRow(iRowFooterCopyRight);

         /*► Static-cell's ... */
         Cell cellCopyRight = rowFooterCopyRight.createCell(1);
         cellCopyRight.setCellValue("La reproducción total o parcial de este documento, constituye una \"COPIA NO CONTROLADA\".");
         cellCopyRight.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.FOOTER_CELL_COPYRIGHT));
         ws.addMergedRegion(new CellRangeAddress(iRowFooterCopyRight, iRowFooterCopyRight, 1, iCellHeader));
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► HEADER: Tag's Dynamic's ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's: ... */
         int iRowTitulo = 0,
             iRowSubtitulo = 2,
             iRowDestinatario = 5,
             iRowServidor = 6,
             iRowFecha = 7;

         XSSFRow rowTitulo = ws.getRow(iRowTitulo),
                 rowSubtitulo = ws.getRow(iRowSubtitulo),
                 rowDestinatario = ws.getRow(iRowDestinatario),
                 rowServidor = ws.getRow(iRowServidor),
                 rowFecha = ws.getRow(iRowFecha);

         /*► Static-Cell's ... */
         // CHunk-01
         Cell cellTitulo = rowTitulo.createCell(5);
         cellTitulo.setCellValue(usrGrupo == RimGrupo.DEPURACION
                                                ? "REGISTRO DE DEPURACIÓN DE DATOS"
                                                : "REGISTRO DE ANALISIS DE DATOS");
         cellTitulo.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TITULO));
         ws.addMergedRegion(new CellRangeAddress(iRowTitulo, iRowTitulo + 1, 5, iCellHeader));

         Cell cellSubtitulo = rowSubtitulo.createCell(5);
         cellSubtitulo.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TITULO));
         ws.addMergedRegion(new CellRangeAddress(iRowSubtitulo, iRowSubtitulo + 1, 5, iCellHeader));
         
         // Chunk-02
         Cell cellDestinatario = rowDestinatario.createCell(iCellHeader - 3);
         cellDestinatario.setCellValue("Destinatario");
         cellDestinatario.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
         ws.addMergedRegion(new CellRangeAddress(iRowDestinatario, iRowDestinatario, iCellHeader - 3, iCellHeader - 2));

         Cell cellDestinatarioValue = rowDestinatario.createCell(iCellHeader - 1);
         cellDestinatarioValue.setCellValue(usrCreadorCargo);
         cellDestinatarioValue.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG_VALUE));
         ws.addMergedRegion(new CellRangeAddress(iRowDestinatario, iRowDestinatario, iCellHeader - 1, iCellHeader));

         // Chunk-02
         Cell cellServidor = rowServidor.createCell(iCellHeader - 3);
         cellServidor.setCellValue("Nombre del Servidor");
         cellServidor.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
         ws.addMergedRegion(new CellRangeAddress(iRowServidor, iRowServidor, iCellHeader - 3, iCellHeader - 2));

         Cell cellServidorValue = rowServidor.createCell(iCellHeader - 1);
         cellServidorValue.setCellValue(usrCreador);
         cellServidorValue.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG_VALUE));
         ws.addMergedRegion(new CellRangeAddress(iRowServidor, iRowServidor, iCellHeader - 1, iCellHeader));

         // Chunk-03
         Cell cellFecha = rowFecha.createCell(iCellHeader - 3);
         cellFecha.setCellValue("Fecha");
         cellFecha.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
         ws.addMergedRegion(new CellRangeAddress(iRowFecha, iRowFecha, iCellHeader - 3, iCellHeader - 2));

         Cell cellFechaValue = rowFecha.createCell(iCellHeader - 1);
         cellFechaValue.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
         cellFechaValue.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG_VALUE));
         ws.addMergedRegion(new CellRangeAddress(iRowFecha, iRowFecha, iCellHeader - 1, iCellHeader));
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► CONFIG-COMMON ... */
         RimanalisisPoiHelper.setBordersToMergedCells(ws);

         /*► OUTPUT ... */
         wb.write(op);
      }

      return new ByteArrayResource(op.toByteArray());
   }

   @Override
   public ByteArrayResource convertReporteMensualProduccionToByteArrResource(String login, int month, int year)throws IOException {

      // ► Dep's ...
      ByteArrayOutputStream oStream = new ByteArrayOutputStream();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      String monthName = YearMonth.of(year, month)
                                  .getMonth()
                                  .getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                                  .toUpperCase();

      // ► Repo dep's ...
      // ------------------------------------------------------------------------------------------------------------------------------
      Usuario usrAnalista = Optional.ofNullable(this.usuarioClient.findByLogin(login).getData())
                                    .orElseThrow(() -> new RimcommonWarningException(Messages.WARNING_USER_NOT_EXISTS));

      UUID idUsrAnalista = usrAnalista.getIdUsuario();

      // ►
      List<ReporteMensualProduccionDto> rptMensualProduccionDb = this.rimproduccionRepository
                                                                                 .findReporteMensualProduccionByParams(idUsrAnalista, month, year);

      if(rptMensualProduccionDb.size() == 0) throw new DataAccessEmptyWarning();

      Integer totalAnalizados = this.getTotalAnalisadosFromReporteMensualProduccionDb(rptMensualProduccionDb);

      // ► ...
      Map<String, List<ReporteMensualProduccionDto>> rptMensualProduccionMap = this.convertReporteMensualProduccionToMap(rptMensualProduccionDb);

      // ------------------------------------------------------------------------------------------------------------------------------
      
      // ► ...
      try (XSSFWorkbook wb = new XSSFWorkbook(rptProduccionMensualXlsx.getInputStream())) {
         
         // ► ...
         XSSFSheet ws = wb.getSheetAt(0);

         /* ► Header-Sheet ... */
         //---------------------------------------------------------------------------------------
         XSSFRow rHeaderAnalista = ws.getRow(3);
         rHeaderAnalista.getCell(2).setCellValue(usrAnalista.getNombres());

         XSSFRow rHeaderMes = ws.getRow(4);
         rHeaderMes.getCell(2).setCellValue(monthName);

         XSSFRow rHeaderTotalAnalizados = ws.getRow(7);
         rHeaderTotalAnalizados.getCell(2).setCellValue(totalAnalizados);
         //---------------------------------------------------------------------------------------

         /* ► Table ... */
         //---------------------------------------------------------------------------------------

         int iRowTable = 11;
         for (Entry<String, List<ReporteMensualProduccionDto>> eRpt : rptMensualProduccionMap.entrySet()) {
            
            // ► ...
            XSSFRow rHeaderTResumen = ws.createRow(iRowTable);

            XSSFCell cHeaderTResumenTitulo = rHeaderTResumen.createCell(1);
            cHeaderTResumenTitulo.setCellValue("TOTAL REPORTE ".concat(eRpt.getKey().toString()));
            cHeaderTResumenTitulo.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.HEADER_TABLE_CELL_RESUMEN_SEMANA));

            XSSFCell cHeaderTResumenBlank = rHeaderTResumen.createCell(2);
            cHeaderTResumenBlank.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.HEADER_TABLE_CELL_RESUMEN_SEMANA));
            cHeaderTResumenBlank = rHeaderTResumen.createCell(4);
            cHeaderTResumenBlank.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.HEADER_TABLE_CELL_RESUMEN_SEMANA));

            XSSFCell cHeaderTResumenTotal = rHeaderTResumen.createCell(3);
            cHeaderTResumenTotal.setCellValue(this.getTotalAnalisadosFromReporteMensualProduccionDb(eRpt.getValue()));
            cHeaderTResumenTotal.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.HEADER_TABLE_CELL_RESUMEN_SEMANA));
            
            // ► ...
            for (ReporteMensualProduccionDto record : eRpt.getValue()) {
               log.info(record.getFechaProd());
               iRowTable++;
               XSSFRow rBodyTable = ws.createRow(iRowTable);

               XSSFCell cBodyTFecha = rBodyTable.createCell(1);
               cBodyTFecha.setCellValue(dateFormat.format(record.getFechaProd()));
               cBodyTFecha.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.BODY_TABLE_CELL));

               XSSFCell cBodyTBase = rBodyTable.createCell(2);
               cBodyTBase.setCellValue(Optional.ofNullable(record.getNombreBase()).orElse("-"));
               cBodyTBase.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.BODY_TABLE_CELL));

               XSSFCell cBodyTProdDia = rBodyTable.createCell(3);
               cBodyTProdDia.setCellValue(Optional.ofNullable(record.getTotalProd()).orElse(0));
               cBodyTProdDia.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.BODY_TABLE_CELL));

               XSSFCell cBodyTObs = rBodyTable.createCell(4);
               cBodyTObs.setCellValue(Optional.ofNullable(record.getObservaciones()).orElse("-"));
               cBodyTObs.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.BODY_TABLE_CELL));
   
            }

            iRowTable++;

         }

         //---------------------------------------------------------------------------------------

         // ► TABLE - FOOTER ...
         //------------------------------------------------------------------------------------------------------------------
         XSSFRow rFooterTTotalProd = ws.createRow(iRowTable);

         XSSFCell cFooterTTitulo = rFooterTTotalProd.createCell(1);
         cFooterTTitulo.setCellValue("PRODUCCIÓN DEL MES");
         cFooterTTitulo.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.FOOTER_TABLE_CELL_RESUMEN_MES));

         XSSFCell cFooterTTotalProd = rFooterTTotalProd.createCell(3);
         cFooterTTotalProd.setCellValue(totalAnalizados);
         cFooterTTotalProd.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.FOOTER_TABLE_CELL_RESUMEN_MES));

         XSSFCell cFooterTResumenBlank =rFooterTTotalProd.createCell(2);
         cFooterTResumenBlank.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.FOOTER_TABLE_CELL_RESUMEN_MES));

         cFooterTResumenBlank = rFooterTTotalProd.createCell(4);
         cFooterTResumenBlank.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, CellType.FOOTER_TABLE_CELL_RESUMEN_MES));

         wb.setSheetName(0, "»".concat(monthName).concat("_").concat(String.valueOf(year)));
         //------------------------------------------------------------------------------------------------------------------
         
         // ► Output ...
         wb.write(oStream);
      } catch (Exception e) {
         log.error(e.getMessage());  
         throw new NotFoundDownloadException();
      }

      return new ByteArrayResource(oStream.toByteArray());
   }

   private List<Map<String, Object>> purgeProduccionAnalisisDb(List<Map<String, Object>> produccionAnalisisDb, String fieldsAssignedCsv){

      /*► DEP'S ... */
      List<Map<String, Object>> produccionAnalisisDbNew = new LinkedList<>();
      String fieldsAux = "dFecha_Creacion, bAnalizado, dFecha_Analisis";

      /*► Proceso de depuración de Data-Set ... */
      produccionAnalisisDb
               .stream()
               .map(prod -> {
                  Map<String, Object> record = new LinkedMap<>();

                  // ► Filtro: Solo campos `Extracción` e `nId` ...
                  for (Entry<String, Object> meta: prod.entrySet()) {
                     
                     /*► Si nombre de campo es `fieldAux`: Interrumpe interacción  ... */
                     if(fieldsAux.contains(meta.getKey())) continue;

                     /*► Si suffix del campo es `_a` y no es un campo asignado: Interrumpe actual interacción  ... */
                     if(meta.getKey().endsWith("_e") || meta.getKey().equals("nId")){
                        record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                     }
                  }

                  // ► Filtro: Solo campos `Analisis` ...
                  for (Entry<String, Object> meta: prod.entrySet()) {

                     /*► Si suffix del campo es `_a` y es un campo asignado: Interrumpe actual interacción  ... */
                     if(meta.getKey().endsWith("_a") && fieldsAssignedCsv.contains(meta.getKey())){
                        record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                     }

                  }
                  
                  return record;
               })
               .forEach(prodNew -> {
                  produccionAnalisisDbNew.add(prodNew);
               });

      return produccionAnalisisDbNew;
   }

   private Map<String, List<ReporteMensualProduccionDto>> convertReporteMensualProduccionToMap(List<ReporteMensualProduccionDto> rptMensualProduccionDb){

      TreeMap<String, List<ReporteMensualProduccionDto>> sortMap = new TreeMap<>();

      sortMap.putAll(rptMensualProduccionDb
                        .stream()
                        .collect(Collectors.groupingBy(ReporteMensualProduccionDto::getSemanaProd)));
      
      
      return sortMap;
      
   }

   private Integer getTotalAnalisadosFromReporteMensualProduccionDb(List<ReporteMensualProduccionDto> rptMensualProduccion){
      return rptMensualProduccion
                     .stream()
                     .map(prod -> prod.getTotalProd())
                     .reduce(0, (acc, next) -> acc + Optional.ofNullable(next).orElse(0));
   }

   //#endregion

}
