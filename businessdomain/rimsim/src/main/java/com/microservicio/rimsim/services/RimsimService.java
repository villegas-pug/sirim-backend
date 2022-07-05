package com.microservicio.rimsim.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

public interface RimsimService {

   List<Map<String, Object>> findTableMetaByName(String nombreTabla);
   
   List<Map<String, Object>> dynamicJoinStatement(String mod, String fields, String where);

   List<Tuple> findDnvByParams(String nacionalidad, String dependencia, String tipoMov, String fecIniMovMig, String fecFinMovMig);

}
