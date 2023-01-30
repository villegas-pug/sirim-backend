package com.microservicio.rimsim.services;

import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import com.microservicio.rimsim.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPorAñosDto;

public interface RimsimService {

   //» v1
   List<Map<String, Object>> findTableMetaByName(String nombreTabla);
   List<Map<String, Object>> dynamicJoinStatement(String mod, String fields, String where);
   List<Tuple> findDnvByParams(String nacionalidad, String dependencia, String tipoMov, String fecIniMovMig, String fecFinMovMig);

   //► DB: `SIM` ...
   List<Pais> findAllPais();
   List<Dependencia> findAllDependencia();
   List<Map<String, Object>> findAllSimUsuario();
   
   //► DB: `BD_SIRIM` ...
   RptPasaportesIndicadoresDto getRptPasaportesIndicadores();
   List<RptPasaportesPorAñosDto> getRptPasaportesEntregadosPorAños();
   List<RptPasaportesPor12UltimosMesesDto> getRptPasaportesEntregadosPor12UltimosMeses();
   List<RptPasaportesPor31UltimosDiasDto> getRptPasaportesEntregadosPor31UltimosDias();

}
