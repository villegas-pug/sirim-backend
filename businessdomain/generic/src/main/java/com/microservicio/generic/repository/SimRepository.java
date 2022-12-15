package com.microservicio.generic.repository;

import java.util.List;
import java.util.Optional;

import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import com.microservicio.generic.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.generic.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.generic.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.generic.models.dto.RptPasaportesPorAñosDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SimRepository extends JpaRepository<Pais, String> {

   @Query(value = "SELECT * FROM SimPais", nativeQuery = true)
   List<Pais> findAllPais();

   @Query(value = "SELECT * FROM SimDependencia", nativeQuery = true)
   List<Dependencia> findAllDependencia();

   @Query(value = "{CALL BD_SIRIM.dbo.usp_Rim_Rpt_Pasaportes_Indicadores()}", nativeQuery = true)
   Optional<RptPasaportesIndicadoresDto> getRptPasaportesIndicadores();

   @Query(value = "{CALL BD_SIRIM.dbo.usp_Rim_Rpt_Pasaportes_Entregados_Por_Años()}", nativeQuery = true)
   List<RptPasaportesPorAñosDto> getRptPasaportesEntregadosPorAños();

   @Query(value = "{CALL BD_SIRIM.dbo.usp_Rim_Rpt_Pasaportes_Entregados_Por_12UltimosMeses()}", nativeQuery = true)
   List<RptPasaportesPor12UltimosMesesDto> getRptPasaportesEntregadosPor12UltimosMeses();

   @Query(value = "{CALL BD_SIRIM.dbo.usp_Rim_Rpt_Pasaportes_Entregados_Por_31UltimosDias()}", nativeQuery = true)
   List<RptPasaportesPor31UltimosDiasDto> getRptPasaportesEntregadosPor31UltimosDias();
   
}
