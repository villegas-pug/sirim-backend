package com.microservicio.rimanalisis.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
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
import com.commons.utils.errors.NotFoundDownloadException;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.helpers.RimcommonHelper;
import com.commons.utils.models.dto.RecordsBetweenDatesDto;
import com.commons.utils.models.dto.RptMensualProduccionDto;
import com.commons.utils.models.dto.RptProduccionHoraLaboralDto;
import com.commons.utils.models.dto.RptTiempoPromedioAnalisisDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.models.enums.RimGrupo;
import com.commons.utils.utils.Response;
import com.google.common.base.Strings;
import com.microservicio.rimanalisis.clients.RimcommonClientRest;
import com.microservicio.rimanalisis.clients.UsuarioClientRest;
import com.microservicio.rimanalisis.errors.RimanalisisWarningException;
import com.microservicio.rimanalisis.helpers.RimanalisisPoiHelper;
import com.microservicio.rimanalisis.helpers.RimanalisisPoiHelper.CellType;
import com.microservicio.rimanalisis.repository.AsigGrupoCamposAnalisisRepository;
import com.microservicio.rimanalisis.repository.ProduccionAnalisisRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
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
   private RimcommonClientRest rimcommonClient;

   @Autowired
   private ProduccionAnalisisRepository repository; 

   @Autowired
   private AsigGrupoCamposAnalisisRepository asigRepository;

   @Autowired
   private UsuarioClientRest usuarioClient;

   //#region Repo - Method's ...

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString) {
      List<Map<String, Object>> recordsAnalisados = this.rimcommonClient.findDynamicSelectStatement(queryString).getData();
      if (recordsAnalisados.size() == 0) throw new DataAccessEmptyWarning();
      return recordsAnalisados;
   }

   @Override
   @Transactional(readOnly = true)
   public LinkedList<RptMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year) {
      return this.repository.findReporteMensualProduccionByParams(idUsrAnalista, month, year);
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptTiempoPromedioAnalisisDto> getRptTiempoPromedioAnalisisByParms(Date fecIni, Date fecFin) {
      List<RptTiempoPromedioAnalisisDto> rptTiempoPromedioAnalisisDb = this.repository.getRptTiempoPromedioAnalisisByParms(fecIni, fecFin);
      if(rptTiempoPromedioAnalisisDb.size() == 0) throw new DataAccessEmptyWarning();
      return rptTiempoPromedioAnalisisDb;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptProduccionHoraLaboralDto> getRptProduccionHorasLaboralesPorAnalista(Date fechaAnalisis, String grupo) {
      List<RptProduccionHoraLaboralDto> produccionHorasLaborales = this.repository.getRptProduccionHorasLaboralesPorAnalista(fechaAnalisis, grupo);
      if(produccionHorasLaborales.size() == 0) throw new DataAccessEmptyWarning();
      return produccionHorasLaborales;
   }

   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisis findAsigGrupoCamposAnalisisById(Long id) {
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis  = this.asigRepository
                                                                     .findById(id)
                                                                     .orElseThrow(DataAccessEmptyWarning::new);
      return asigGrupoCamposAnalisis;
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> getRptS10DRCMFR001Produccion(String nombreTabla, Date fecIni, Date fecFin, boolean isRoot, Long idAsig) {
      List<Map<String, Object>> rs = DataModelHelper.convertTuplesToJson(this.repository.getRptS10DRCMFR001Produccion(nombreTabla, fecIni, fecFin, isRoot, idAsig), false);
      return rs;
   }

   @Override
   @Transactional
   public void saveProduccionAnalisis(ProduccionAnalisis produccionAnalisis) {
      this.repository.save(produccionAnalisis);
   }

   @Override
   @Transactional(readOnly = true)
   public ProduccionAnalisis findProduccionAnalisisById(Long idProdAnalisis) {
      ProduccionAnalisis produccionAnalisis = this.repository.findById(idProdAnalisis)
                                                             .orElseThrow(DataAccessEmptyWarning::new);
      return produccionAnalisis;
   }

   @Override
   @Transactional
   public void setTerminadoProduccionAnalisis(Long idProdAnalisis) {
      ProduccionAnalisis produccionAnalisis = this.findProduccionAnalisisById(idProdAnalisis);
      produccionAnalisis.setTerminado(!produccionAnalisis.isTerminado());
      this.saveProduccionAnalisis(produccionAnalisis);
   }

   //#endregion

   //#region Client method's ...

   @Override
   public Response<Usuario> findUsuarioByLogin(String login) {
      return Optional
               .ofNullable(this.usuarioClient.findByLogin(login))
               .orElseThrow(() -> new RimanalisisWarningException(Messages.WARNING_USER_NOT_EXISTS));
   }

   //#endregion

   //#region: Custom method's ...

   @Override
   public ByteArrayResource convertProduccionAnalisisToByteArrResource(RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException{

      // ► Dep's ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = null;
      boolean isRequestOfRoot = recordsBetweenDatesDto.getIdAsigGrupo() == null ? true : false;
      RimGrupo usrGrupo = null;
      String nombreTabla = "-",
             usrAnalista = "-",
             usrAnalistaCargo = recordsBetweenDatesDto.getUsr().getCargo(),
             usrCreador = "-",
             usrCreadorCargo = "-",
             metafieldsACsv = "-",
             metafieldsECsv = "-";
      boolean isAssignedTemplate = recordsBetweenDatesDto.isAssignedTemplate();;
      Date dateOfFirstAnalisis = null;
      
      if (isRequestOfRoot) { // Petición del coordinador ...
         
         nombreTabla = recordsBetweenDatesDto.getNombreTabla();
         usrGrupo = recordsBetweenDatesDto.getUsr().getGrupo();

         TablaDinamicaDto tablaDinamica = this.rimcommonClient.findTablaDinamicaByNombre(nombreTabla).getData();

         metafieldsECsv = tablaDinamica.getMetaFieldsCsv();
         metafieldsACsv = tablaDinamica.getLstGrupoCamposAnalisis().get(0).getMetaFieldsCsv();

      } else { // Petición del analista ...

         // ► Repo dep's ...
         asigGrupoCamposAnalisis = this.asigRepository
                                             .findById(recordsBetweenDatesDto.getIdAsigGrupo())
                                             .orElseThrow(DataAccessEmptyWarning::new);
   
         if(asigGrupoCamposAnalisis == null)
            throw new NotFoundDownloadException();
         
         usrGrupo = asigGrupoCamposAnalisis.getUsrAnalista().getGrupo();
         nombreTabla = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getNombre();
         usrAnalista = asigGrupoCamposAnalisis.getUsrAnalista().getNombres();
         usrCreador = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getUsrCreador().getNombres();
         usrCreadorCargo = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getUsrCreador().getCargo();
         metafieldsACsv = asigGrupoCamposAnalisis.getGrupo().getMetaFieldsCsv();
         metafieldsECsv = asigGrupoCamposAnalisis.getGrupo().getTablaDinamica().getMetaFieldsCsv();
         dateOfFirstAnalisis = asigGrupoCamposAnalisis.getProduccionAnalisis().size() > 0
                                       ? asigGrupoCamposAnalisis
                                                .getProduccionAnalisis()
                                                .stream()
                                                .sorted(Comparator.comparing(ProduccionAnalisis::getFechaFin))
                                                .collect(Collectors.toList())
                                                .get(0)
                                                .getFechaFin()
                                       : new Date();
                                       

      }


      // ► Data-Set ...
      //------------------------------------------------------------------------------------------------------
      List<Map<String, Object>> ds;

      // Dep's ...
      Date fecIni = recordsBetweenDatesDto.getFecIni(),
           fecFin = recordsBetweenDatesDto.getFecFin();

      if (isRequestOfRoot){ // Petición del coordinador ...

         if (isAssignedTemplate) {// Backup: Base completa  ...

            // Dep's ...
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT *, [Control_calidad_qc] = '', [criterios_errados_qc] = '', [Detalle_error_qc] = '', [%_error_qc] = '' FROM ")
                       .append(nombreTabla);

            ds = this.rimcommonClient.findDynamicSelectStatement(queryString.toString()).getData();

         } else {

            ds = this.getRptS10DRCMFR001Produccion(nombreTabla, fecIni, fecFin, true, 0L);

         }
         
      } else { // Petición del analista ...

         if (isAssignedTemplate) {// Registros asignados ...

            // Dep's ...
            int regIni = asigGrupoCamposAnalisis.getRegAnalisisIni(),
                regFin = asigGrupoCamposAnalisis.getRegAnalisisFin();

            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT *, [Control_calidad_qc] = '', [criterios_errados_qc] = '', [Detalle_error_qc] = '', [%_error_qc] = '' FROM ").append(nombreTabla)
                       .append(" WHERE nId BETWEEN ")
                       .append(regIni).append(" AND ").append(regFin);

            ds = this.rimcommonClient.findDynamicSelectStatement(queryString.toString()).getData();

         } else { // Registros analizados ...

            ds = this.getRptS10DRCMFR001Produccion(nombreTabla, fecIni, fecFin, false, recordsBetweenDatesDto.getIdAsigGrupo());

         }

      }

      if(ds.size() == 0) throw new NotFoundDownloadException();

      List<Map<String, Object>> dsNew = this.purgeProduccionAnalisisDb(ds);
      //------------------------------------------------------------------------------------------------------

      ByteArrayOutputStream op = new ByteArrayOutputStream();

      try (XSSFWorkbook wb = new XSSFWorkbook(rptProduccionDiarioXlsx.getInputStream())) {

         XSSFSheet ws = wb.getSheetAt(0);
         
         // ► GLOBAL-DEP'S ...
         int rowIndexHeader = 10;
         int columnWidth = 6000;
         Cell dynamicCell;

         // ► HEADER: Tag's Static's ...
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         // ► Dep's: ...
         XSSFRow rowCodigo = ws.getRow(3);
         if (isRequestOfRoot){

            rowCodigo.getCell(1).setCellValue(
                                                usrGrupo == RimGrupo.DEPURACION 
                                                   ? "S10.DRCM.FR.004"
                                                   : "S10.DRCM.FR.005");

         } else {
            
            rowCodigo.getCell(1).setCellValue(
                                             usrGrupo == RimGrupo.DEPURACION 
                                                ? "S10.DRCM.FR.001"
                                                : "S10.DRCM.FR.001");

         }

         XSSFRow rowVersion = ws.getRow(3);
         rowVersion.getCell(4).setCellValue(usrGrupo == RimGrupo.DEPURACION ? "'01"  : "'01");

         XSSFRow rowServidorCargo = ws.getRow(5);
         Cell cellTagServidorCargo = rowServidorCargo.getCell(1);
         cellTagServidorCargo.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
         Cell cellServidorCargo = rowServidorCargo.getCell(5);
         cellServidorCargo.setCellValue(usrAnalistaCargo);

         XSSFRow rowNombreRemite = ws.getRow(6);
         Cell cellTagNombreRemite = rowNombreRemite.getCell(1);
         cellTagNombreRemite.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
         Cell cellServidorRemite = rowNombreRemite.getCell(5);
         cellServidorRemite.setCellValue(usrAnalista);

         XSSFRow rowNombreBase = ws.getRow(7);
         Cell cellTagNombreBase = rowNombreBase.getCell(1);
         cellTagNombreBase.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_TAG));
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
         // N° → sId ...
         iCellHeader++;
         Cell cellNro = rowHeader.createCell(iCellHeader);
         cellNro.setCellValue("N°");
         cellNro.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_EXTRACCION));
         ws.setColumnWidth(iCellHeader, 2500);

         /* iCellHeader++;
         Cell cellId = rowHeader.createCell(iCellHeader);
         cellId.setCellValue("ID");
         cellId.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_EXTRACCION));
         ws.setColumnWidth(iCellHeader, 2500); */
         
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

         if (isRequestOfRoot) { // Petición del coordinador ...

            iCellHeader++;
            Cell cellAnalista = rowHeader.createCell(iCellHeader);
            cellAnalista.setCellValue("Analista");
            cellAnalista.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_AUX));
            ws.setColumnWidth(iCellHeader, 3000);

            iCellHeader++;
            Cell cellFecA = rowHeader.createCell(iCellHeader);
            cellFecA.setCellValue("Fecha Analisis");
            cellFecA.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_AUX));
            ws.setColumnWidth(iCellHeader, 3000);


            iCellHeader++;
            Cell cellBase = rowHeader.createCell(iCellHeader);
            cellBase.setCellValue("Nombre de Base de Datos");
            cellBase.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_AUX));
            ws.setColumnWidth(iCellHeader, 3500);

            iCellHeader++;
            Cell cellQC = rowHeader.createCell(iCellHeader);
            cellQC.setCellValue("Control de calidad ");
            cellQC.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_AUX));
            ws.setColumnWidth(iCellHeader, 3500);
            
         }

         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► HEADER-TABLE: Control de Calidad ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         
         if (!isRequestOfRoot) {

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

         }
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         // ► HEADER-TABLE: Info ...
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         // ► Dep's ...
         XSSFRow rowInfo = ws.createRow(rowIndexHeader + 1);
         rowInfo.setHeightInPoints((short) 68.25);
         int iCellInfo = 0;

         // ► Static-cell: Chunk-01 ...
         CellStyle cellInfoStyle = RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.HEADER_CELL_INFO);
         iCellInfo++;
         cellNro = rowInfo.createCell(iCellInfo);
         cellNro.setCellValue("Colocar ítem");
         cellNro.setCellStyle(cellInfoStyle);

         /* iCellInfo++;
         cellId = rowInfo.createCell(iCellInfo);
         cellId.setCellValue("Colocar el código de identificación del registro en la base de datos");
         cellId.setCellStyle(cellInfoStyle); */

         // ► Dynamic-cell's ...
         Map<String, String> metaInfoMap = RimcommonHelper.convertMetaFieldsEAndACsvToMetaInfoMap(metafieldsECsv, metafieldsACsv);
         for (Entry<String, Object> record : dsNew.get(0).entrySet()) {
            if(record.getKey().endsWith("_e") || record.getKey().endsWith("_a")){
               iCellInfo++;
               dynamicCell = rowInfo.createCell(iCellInfo);
               dynamicCell.setCellValue(metaInfoMap.get(record.getKey()));
               dynamicCell.setCellStyle(cellInfoStyle);
            }
         }

         if (isRequestOfRoot) { // Petición de `Coordinador`

            iCellInfo++;
            Cell cellAnalista = rowInfo.createCell(iCellInfo);
            cellAnalista.setCellValue("Colocar usuario del analista");
            cellAnalista.setCellStyle(cellInfoStyle);

            iCellInfo++;
            Cell cellFecA = rowInfo.createCell(iCellInfo);
            cellFecA.setCellValue("Colocar la fecha del análisis");
            cellFecA.setCellStyle(cellInfoStyle);

            iCellInfo++;
            Cell cellBase = rowInfo.createCell(iCellInfo);
            cellBase.setCellValue("Colocar el número de la base de datos analizada");
            cellBase.setCellStyle(cellInfoStyle);

            iCellInfo++;
            Cell cellQC = rowInfo.createCell(iCellInfo);
            cellQC.setCellValue("Colocar \"SI\"  en el caso que el regitro sea parte de la muestra de control de calidad, caso contrario consignar \"NO\" ");
            cellQC.setCellStyle(cellInfoStyle);

         }

         // ► Static-cell: Chunk-02 ...
         if (!isRequestOfRoot) { // Petición de `Analista`

            iCellInfo++;
            Cell cellCtrlCal = rowInfo.createCell(iCellInfo);
            cellCtrlCal.setCellValue("Colocar \"SI\"  en el caso que el regitro sea parte de la muestra de control de calidad, caso contrario consignar \"NO\" ");
            cellCtrlCal.setCellStyle(cellInfoStyle);
   
            iCellInfo++;
            Cell cellCriteriosErr = rowInfo.createCell(iCellInfo);
            cellCriteriosErr.setCellValue("Colocar la cantidad de criterios errados.");
            cellCriteriosErr.setCellStyle(cellInfoStyle);
            
            iCellInfo++;
            Cell cellDetalleErr = rowInfo.createCell(iCellInfo);
            cellDetalleErr.setCellValue("Especificar el/los criterio(s) incorrecto(s)");
            cellDetalleErr.setCellStyle(cellInfoStyle);
   
            iCellInfo++;
            Cell cellPorcErr = rowInfo.createCell(iCellInfo);
            cellPorcErr.setCellValue("Colocar el % del error en función al número de criterios de análisis");
            cellPorcErr.setCellStyle(cellInfoStyle);

         }
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► BODY: Registros de `Extracción`, `Analisis` y `Control Calidad` ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         // Dep's ...
         int iR = 0,
             iI = 0,
             iRowAnalisis = 11;

         CellStyle bodyCellStyle = RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS);

         for (Map<String, Object> record : dsNew) {
            iR++;
            XSSFRow rowAnalisis = ws.createRow(iRowAnalisis + iR);
            
            // N°
            /*iI++;
            Cell cellBody = rowAnalisis.createCell(iI);
            cellBody.setCellStyle(bodyCellStyle);
            cellBody.setCellValue(String.valueOf(iR));*/

            // ► Dynamic-Cell: N° → `sId`, `Aux`, `E`, `A` y `QC` ...
            for (Entry<String, Object> item : record.entrySet()) {
               iI++;
               Cell cellBody = rowAnalisis.createCell(iI);
               cellBody.setCellStyle(bodyCellStyle);

               try {
                  cellBody.setCellValue((double)item.getValue());
               } catch (Exception e) {
                  cellBody.setCellValue(Optional.ofNullable(item.getValue()).orElse("-").toString());
               }

            }

            // Cleanup ...
            iI = 0;
         }

         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► FOOTER: Totales ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         // ► Dep's ...
         int iRowFooter = ws.getPhysicalNumberOfRows();
         int totalFieldsFromDS = (int) dsNew.get(0).values().stream().count();

         if (!isRequestOfRoot) { // Petición de `Analista` ...

            XSSFRow rowTotal = ws.createRow(iRowFooter);
            
            // ► Static-cell's ...
            Cell cellEstado = rowTotal.createCell(totalFieldsFromDS - 3);
            cellEstado.setCellValue("Estado");
            cellEstado.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.FOOTER_CELL_TOTAL_TAG));
   
            Cell cellValidado = rowTotal.createCell(totalFieldsFromDS - 2);
            cellValidado.setCellValue(asigGrupoCamposAnalisis.isCtrlCalConforme() ? "VALIDADO" : "OBSERVADO");
            cellValidado.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));
   
            Cell cellErr = rowTotal.createCell(totalFieldsFromDS - 1);
            cellErr.setCellValue("% error promedio");
            cellErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.FOOTER_CELL_TOTAL_TAG));
            
            // ► Dynamic-cell's ...
            Cell cellPercentErr = rowTotal.createCell(totalFieldsFromDS);
            String cellLetterPercentErr = CellReference.convertNumToColString(cellPercentErr.getColumnIndex()),
                   formulaPercentErr = "IFERROR(ROUND(AVERAGE(".concat(cellLetterPercentErr)
                                                       .concat(String.valueOf(iRowAnalisis + 2))
                                                       .concat(":")
                                                       .concat(cellLetterPercentErr)
                                                       .concat(String.valueOf(iRowFooter))
                                                       .concat(")")
                                                       .concat(", 0)")
                                                       .concat("&\"%\"")
                                                       .concat(", \"-\")");
               
            cellPercentErr.setCellFormula(formulaPercentErr);
            cellPercentErr.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));

         }
         //------------------------------------------------------------------------------------------------------------------------------------------------------

         /*► FOOTER: Obs ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's  */
         iRowFooter += 2;
         XSSFRow rowObs = ws.createRow(iRowFooter);
         rowObs.setHeightInPoints((short) 78.75);
         
         /*► Static-cell's ... */
         String obsQCCsv = "";
         if (!isRequestOfRoot) {

            obsQCCsv = asigGrupoCamposAnalisis
                              .getProduccionAnalisis()
                              .stream()
                              .filter(p -> p.isRevisado() && !Strings.isNullOrEmpty(p.getMetaFieldIdErrorCsv()))
                              .map(p -> p.getObservacionesCtrlCal())
                              .collect(Collectors.joining(", "));
            
         }

         Cell cellObs = rowObs.createCell(1);
         cellObs.setCellValue("OBSERVACIONES:\n- ".concat(obsQCCsv));
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

         if (isRequestOfRoot){

            cellTitulo.setCellValue(usrGrupo == RimGrupo.DEPURACION
                                                   ? "REGISTRO CONSOLIDADO DE DEPURACIÓN DE DATOS"
                                                   : "REGISTRO CONSOLIDADO DE ANÁLISIS DE INFORMACIÓN");

         } else {

            cellTitulo.setCellValue(usrGrupo == RimGrupo.DEPURACION
                                                   ? "REGISTRO DE DEPURACIÓN DE DATOS"
                                                   : "REGISTRO DE ANÁLISIS DE INFORMACIÓN");

         }

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
         cellFechaValue.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(isRequestOfRoot ? new Date() : dateOfFirstAnalisis));
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
                                    .orElseThrow(() -> new RimanalisisWarningException(Messages.WARNING_USER_NOT_EXISTS));

      UUID idUsrAnalista = usrAnalista.getIdUsuario();

      // ►
      List<RptMensualProduccionDto> rptMensualProduccionDb = this.repository.findReporteMensualProduccionByParams(idUsrAnalista, month, year);

      if(rptMensualProduccionDb.size() == 0) throw new DataAccessEmptyWarning();

      Integer totalAnalizados = this.getTotalAnalisadosFromReporteMensualProduccionDb(rptMensualProduccionDb);

      // ► ...
      Map<String, List<RptMensualProduccionDto>> rptMensualProduccionMap = this.convertReporteMensualProduccionToMap(rptMensualProduccionDb);

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
         for (Entry<String, List<RptMensualProduccionDto>> eRpt : rptMensualProduccionMap.entrySet()) {
            
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
            for (RptMensualProduccionDto record : eRpt.getValue()) {
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

   private List<Map<String, Object>> purgeProduccionAnalisisDb(List<Map<String, Object>> produccionAnalisisDb){

      // ► Dep's ...
      List<Map<String, Object>> produccionAnalisisDbNew = new LinkedList<>();
      String fieldsAux = "dFecha_Creacion, bAnalizado, dFecha_Analisis";

      // ► Proceso de depuración de Data-Set ...
      produccionAnalisisDb
            .stream()
            .map(prod -> {
               Map<String, Object> record = new LinkedMap<>();

               // ► Filtro: Solo campos `Extracción` ...
               for (Entry<String, Object> meta: prod.entrySet()) {
                  // ► Si suffix del campo es `_a` y no es un campo asignado ...
                  if (meta.getKey().endsWith("_e") || meta.getKey().equals("nId")){
                     record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                  }
               }

               // ► Filtro: Solo campos `Analisis` ...
               for (Entry<String, Object> meta: prod.entrySet()) {
                  // ► Si suffix del campo es `_a` ...
                  if (meta.getKey().endsWith("_a")){
                     record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                  }

               }

               // ► Filtro: No campos de `E` y `A` ...
               for (Entry<String, Object> meta: prod.entrySet()) {
                  
                  // ► Si nombre de campo es `fieldAux`: Interrumpe interacción  ...
                  if (fieldsAux.contains(meta.getKey())) continue;

                  // ► Si suffix de campo no es `_e` y `_a` ...
                  if (!meta.getKey().endsWith("_e") && !meta.getKey().endsWith("_a") && !meta.getKey().equals("nId")){

                     try {
                        record.put(meta.getKey(), (double) meta.getValue());
                     } catch (Exception e) {
                        record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                     }

                  }
               }
               
               return record;
            })
            .forEach(prodNew -> {
               produccionAnalisisDbNew.add(prodNew);
            });

      return produccionAnalisisDbNew;
   }

   private Map<String, List<RptMensualProduccionDto>> convertReporteMensualProduccionToMap(List<RptMensualProduccionDto> rptMensualProduccionDb){

      TreeMap<String, List<RptMensualProduccionDto>> sortMap = new TreeMap<>();

      sortMap.putAll(rptMensualProduccionDb
                        .stream()
                        .collect(Collectors.groupingBy(RptMensualProduccionDto::getSemanaProd)));
      
      
      return sortMap;
      
   }

   private Integer getTotalAnalisadosFromReporteMensualProduccionDb(List<RptMensualProduccionDto> rptMensualProduccion){
      return rptMensualProduccion
                     .stream()
                     .map(prod -> prod.getTotalProd())
                     .reduce(0, (acc, next) -> acc + Optional.ofNullable(next).orElse(0));
   }

   //#endregion

}
