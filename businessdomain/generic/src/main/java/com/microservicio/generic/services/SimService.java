package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import com.microservicio.generic.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.generic.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.generic.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.generic.models.dto.RptPasaportesPorAñosDto;

public interface SimService {

   // ► DB: `SIM` ...
   List<Pais> findAll();
   List<Dependencia> findAllDependencia();
   
   // ► DB: `BD_SIRIM` ...
   RptPasaportesIndicadoresDto getRptPasaportesIndicadores();
   List<RptPasaportesPorAñosDto> getRptPasaportesEntregadosPorAños();
   List<RptPasaportesPor12UltimosMesesDto> getRptPasaportesEntregadosPor12UltimosMeses();
   List<RptPasaportesPor31UltimosDiasDto> getRptPasaportesEntregadosPor31UltimosDias();

}
