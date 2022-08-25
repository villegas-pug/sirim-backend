package com.microservicio.rimextraccion.helpers;

import java.util.List;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RimanalisisPoiHelper {

   /* // ► ...
   public static final String HEADER_CELL_TITULO = "HEADER_CELL_TITULO";
   public static final String HEADER_CELL_TAG = "HEADER_CELL_TAG";
   public static final String HEADER_CELL_TAG_VALUE = "HEADER_CELL_TAG_VALUE";
   public static final String HEADER_CELL_ANALISIS = "HEADER_CELL_ANALISIS";
   public static final String HEADER_CELL_EXTRACCION = "HEADER_CELL_EXTRACCION";
   public static final String HEADER_CELL_INFO = "HEADER_CELL_INFO";
   public static final String HEADER_CELL_CONTROL_CALIDAD = "HEADER_CELL_CONTROL_CALIDAD";
   public static final String BODY_CELL_ANALISIS = "BODY_CELL_ANALISIS";
   public static final String FOOTER_CELL_OBS = "FOOTER_CELL_OBS";
   public static final String FOOTER_CELL_LEYENDA_2 = "FOOTER_CELL_LEYENDA_2";
   public static final String FOOTER_CELL_COPYRIGHT = "FOOTER_CELL_COPYRIGHT";

   // ► `ReporteMensualProduccion` ...
   public static final String HEADER_TABLE_CELL_RESUMEN_SEMANA = "HEADER_TABLE_CELL_RESUMEN_SEMANA";
   public static final String BODY_TABLE_CELL = "BODY_TABLE_CELL"; */

   public enum CellType {
      HEADER_CELL_TITULO,
      HEADER_CELL_TAG,
      HEADER_CELL_TAG_VALUE,
      HEADER_CELL_ANALISIS,
      HEADER_CELL_EXTRACCION,
      HEADER_CELL_CONTROL_CALIDAD,
      HEADER_CELL_INFO,
      BODY_CELL_ANALISIS,
      FOOTER_CELL_OBS,
      FOOTER_CELL_COPYRIGHT,
      FOOTER_CELL_LEYENDA_2,

      // ► `ReporteMensualProduccion` ...
      HEADER_TABLE_CELL_RESUMEN_SEMANA,
      BODY_TABLE_CELL,
      FOOTER_TABLE_CELL_RESUMEN_MES
   }

   public static CellStyle createCellStyle(XSSFWorkbook wb, CellType type){

      // ► DEP'S ...
      CellStyle cellStyle = wb.createCellStyle();
      Font font = wb.createFont();

      // ► Apply-Style: `Font` ...
      font.setFontName("Arial");
      font.setFontHeightInPoints((short) 11);
      font.setColor(HSSFColorPredefined.WHITE.getIndex());
      cellStyle.setFont(font);

      // ► Apply-Style: `Align` ...
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

      // ► Apply-Style: `Border` ...
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);

      cellStyle.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
      cellStyle.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
      cellStyle.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
      cellStyle.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());

      // ► Apply-Style: `Text` ...
      cellStyle.setWrapText(true);

      // ► Apply-Style: `Color` ...
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      switch (type) {
         case HEADER_CELL_TITULO:
            font.setBold(true);
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            break;
         case HEADER_CELL_TAG:
            font.setBold(true);
            font.setColor(HSSFColorPredefined.WHITE.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.DARK_BLUE.getIndex());
            break; 
         case HEADER_CELL_TAG_VALUE:
            font.setBold(true);
            font.setItalic(true);
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            break;
         case HEADER_CELL_EXTRACCION:
            cellStyle.setFillForegroundColor(HSSFColorPredefined.DARK_BLUE.getIndex());
            break;
         case HEADER_CELL_ANALISIS:
            cellStyle.setFillForegroundColor(HSSFColorPredefined.DARK_YELLOW.getIndex());
            break;
         case HEADER_CELL_CONTROL_CALIDAD:
            cellStyle.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());
            break;
         case HEADER_CELL_INFO:
            font.setFontHeightInPoints((short) 8);
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.GREY_25_PERCENT.getIndex());
            break;
         case BODY_CELL_ANALISIS:
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            font.setFontHeightInPoints((short) 9);
            cellStyle.setWrapText(false);
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            break;
         case FOOTER_CELL_OBS:
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            break;
         case FOOTER_CELL_COPYRIGHT:
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            font.setFontHeightInPoints((short) 8);
            break;
         case FOOTER_CELL_LEYENDA_2:
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            break;
         case HEADER_TABLE_CELL_RESUMEN_SEMANA:
            font.setBold(true);
            font.setFontHeightInPoints((short) 10);
            cellStyle.setFillForegroundColor(HSSFColorPredefined.AQUA.getIndex());
            break;
         case BODY_TABLE_CELL:
            font.setColor(HSSFColorPredefined.BLACK.getIndex());
            font.setFontHeightInPoints((short) 8);
            cellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
            break;
         case FOOTER_TABLE_CELL_RESUMEN_MES:
            font.setColor(HSSFColorPredefined.WHITE.getIndex());
            font.setBold(true);
            font.setFontHeightInPoints((short) 10);
            cellStyle.setFillForegroundColor(HSSFColorPredefined.CORAL.getIndex());
            break;
         default:
            break;
      }

      return cellStyle;

   }

   public static void setBordersToMergedCells(XSSFSheet ws){

      // ► Dep's ...
      List<CellRangeAddress> mergeRegions =  ws.getMergedRegions();

      /*► Apply-Style ... */
      for (CellRangeAddress cellRangeAddress : mergeRegions) {
         RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, ws);
         RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, ws);
         RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, ws);
         RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, ws);
      }

   }
   
}
