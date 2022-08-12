package com.microservicio.rimextraccion.helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RimcommonHelper {

   public enum DecorateType {
      INI,
      FIN
   }

   public static final String INI = "INI";
   public static final String FIN = "FIN";

   public static Stream<String> convertMetaFieldsCsvToMetaStream(String metaFieldsCsv){
      return  Arrays.stream(metaFieldsCsv.split(","))
                    .map(f -> f.trim());
   }

   public static String convertMetaFieldsCsvToFieldsNameCsv(String metaFieldsCsv){
      return  Arrays.stream(metaFieldsCsv.split(","))
                    .map(f -> f.trim())
                    .map(f -> f.split("\\|")[0].trim())
                    .collect(Collectors.joining(","));
   }

   public static Map<String, String> convertMetaFieldsEAndACsvToMetaInfoMap(String metaFieldsCsvE, String metaFieldsCsvA){

      /*► Dep's ... */
      Map<String, String> metaInfomap = new HashMap<>();
      Stream<String> metaFieldsE = convertMetaFieldsCsvToMetaStream(metaFieldsCsvE);
      Stream<String> metaFieldsA = convertMetaFieldsCsvToMetaStream(metaFieldsCsvA);

      /*► Return ...  */
      metaFieldsE
         .forEach(f -> {
            metaInfomap.put(f.split("\\|")[0].trim(), f.split("\\|")[2].trim());
         });

      metaFieldsA
         .forEach(f -> {
            metaInfomap.put(f.split("\\|")[0].trim(), f.split("\\|")[2].trim());
         });

      return metaInfomap;
   }

   public static String convertDateToSqlDateTimeStr(Date date, DecorateType type){

      LocalDate lDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

      int year = lDate.getYear();
      int month = lDate.getMonth().getValue();
      int day = lDate.getDayOfMonth();

      StringBuilder sqlDateTimeStr = new StringBuilder();
      sqlDateTimeStr.append("'").append(year).append("-").append(month).append("-").append(day);

      switch (type) {
         case INI:
            sqlDateTimeStr.append(" 00:00:00.000").append("'");
         break;
         default:
            sqlDateTimeStr.append(" 23:59:59.999").append("'");
      }


      return sqlDateTimeStr.toString();

   }

   public static String undecoratedMetaFieldName(String metaFieldName){
      return metaFieldName.replaceAll("^[snbd]|_a$|_e$|[._]", " ").trim();
   }

   public static void main(String[] args) {
      String undecorated = undecoratedMetaFieldName("dFecha_Control_e");
      System.out.println(undecorated);
   }

}