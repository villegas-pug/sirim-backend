package com.commons.utils.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;

public class DataModelHelper{

   public static List<Map<String, Object>> convertTuplesToJson(List<Tuple> tuples, Boolean removePrefix){
      
      /*► Repo ... */
      List<Map<String, Object>> metaFields = new ArrayList<>();

      tuples.forEach(tuple -> {
         Map<String, Object> metaField = new LinkedHashMap<>();

         tuple.getElements().forEach(e -> {
            if(removePrefix)
               metaField.put(removePrefixOfFieldName(e.getAlias()), tuple.get(e.getAlias()));
            else 
               metaField.put(e.getAlias(), tuple.get(e.getAlias()));
         });
         metaFields.add(metaField);
      });

      return metaFields;
            
   }

   public static String removePrefixOfFieldName(String fieldName){
      return fieldName.substring(1, 2).toLowerCase().concat(fieldName.substring(2));
   }

}