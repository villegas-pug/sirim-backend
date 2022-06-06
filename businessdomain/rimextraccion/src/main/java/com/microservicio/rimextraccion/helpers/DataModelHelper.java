package com.microservicio.rimextraccion.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;

public class DataModelHelper{

   public static List<Map<String, String>> convertMetaFields(List<Tuple> tuples){
      
      List<Map<String, String>> metaFields = new ArrayList<>();

      tuples.forEach(tuple -> {
         Map<String, String> metaField = new HashMap<>();

         tuple.getElements().forEach(element -> {
            metaField.put(element.getAlias(), tuple.get(element.getAlias()).toString());
         });
         metaFields.add(metaField);
      });

      return metaFields;
            
   }

}