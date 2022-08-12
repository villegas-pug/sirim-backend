package com.microservicio.rimextraccion.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.microservicio.rimextraccion.dto.AnalizadosDto;
import com.microservicio.rimextraccion.errors.NotFoundDownloadException;
import com.microservicio.rimextraccion.helpers.RimanalisisPoiHelper;
import com.microservicio.rimextraccion.helpers.RimcommonHelper;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RimanalisisServiceImpl implements RimanalisisService {

   @Value("classpath:static/S10.DRCM.FR.001-Registro de depuracion de datos_V02.xlsx")
   private Resource prodAnalisisXlsx;

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired 
   private RimasigGrupoCamposAnalisisService rimasigAnalisisService;

   @Override
   public ByteArrayResource convertProduccionAnalisisToByteArrResource(AnalizadosDto analizadosDto) throws IOException{

      /*► DEP'S ... */
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigAnalisisService.findById(analizadosDto.getIdAsigGrupo());
      if(asigGrupoCamposAnalisis == null)
         throw new NotFoundDownloadException();

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
                              .filter(prod -> prod.getFechaFin().compareTo(analizadosDto.getFecIni()) >= 0 
                                              && prod.getFechaFin().compareTo(analizadosDto.getFecFin()) <= 0 )
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
      if(ds.size() == 0)
         throw new NotFoundDownloadException();

      List<Map<String, Object>> dsNew = this.purgeProduccionAnalisisDb(ds, metaNameAssignedCsv);
      //------------------------------------------------------------------------------------------------------

      ByteArrayOutputStream op = new ByteArrayOutputStream();

      try (XSSFWorkbook wb = new XSSFWorkbook(prodAnalisisXlsx.getInputStream())) {

         XSSFSheet ws = wb.getSheetAt(0);
         
         /*► GLOBAL-DEP'S ... */
         int rowIndexHeader = 10;
         int columnWidth = 6000;
         Cell dynamicCell;

         /*► HEADER: Tag's Static's ... */
         //------------------------------------------------------------------------------------------------------------------------------------------------------
         /*► Dep's: ... */
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

         /*► BODY: Registros de analisis ... */
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

            /*► Dynamic-Cell ... */
            for (Entry<String, Object> item : record.entrySet()) {
               iI++;
               cellAnalisis = rowAnalisis.createCell(iI);
               cellAnalisis.setCellStyle(RimanalisisPoiHelper.createCellStyle(wb, RimanalisisPoiHelper.CellType.BODY_CELL_ANALISIS));
               cellAnalisis.setCellValue(Optional.ofNullable(item.getValue()).orElse("-").toString());
            }

            /*► Static-Cell ... */
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
         cellTitulo.setCellValue("REGISTRO DE DEPURACIÓN DE DATOS");
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
   
   //#region: Private method's ...

   private List<Map<String, Object>> purgeProduccionAnalisisDb(List<Map<String, Object>> produccionAnalisisDb, String fieldsAssignedCsv){

      /*► DEP'S ... */
      List<Map<String, Object>> produccionAnalisisDbNew = new LinkedList<>();
      String fieldsAux = "dFecha_Creacion, bAnalizado, dFecha_Analisis";

      /*► Proceso de depuración de Data-Set ... */
      produccionAnalisisDb
               .stream()
               .map(prod -> {
                  Map<String, Object> record = new LinkedMap<>();
                  for (Entry<String, Object> meta: prod.entrySet()) {
                     /*► Si suffix del campo es `_a` y no es un campo asignado: Interrumpe actual interacción  ... */
                     if(meta.getKey().endsWith("_a") && !fieldsAssignedCsv.contains(meta.getKey())){
                        continue;
                     }
                     
                     /*► Si nombre de campo es ...: Interrumpe actual interacción  ... */
                     if(fieldsAux.contains(meta.getKey())){
                        continue;
                     }

                     record.put(meta.getKey(), Optional.ofNullable(meta.getValue()).orElse("-"));
                  }
                  
                  return record;
               })
               .forEach(prodNew -> {
                  produccionAnalisisDbNew.add(prodNew);
               });

      return produccionAnalisisDbNew;
   }

   //#endregion

}
