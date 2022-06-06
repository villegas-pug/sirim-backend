package com.microservicio.rimsim.services;

import java.util.List;
import java.util.Map;

public interface RimsimService {

   List<Map<String, String>> findTableMetaByName(String nombreTabla);
   
   List<Object[]> dynamicJoinStatement(String mod, String fields, String where);

}
