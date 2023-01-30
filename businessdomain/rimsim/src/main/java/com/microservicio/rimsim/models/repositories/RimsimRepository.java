package com.microservicio.rimsim.models.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.MovMigra;
import com.commons.utils.models.entities.Pais;
import com.commons.utils.models.repositories.CommonRepository;
import com.microservicio.rimsim.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPorAñosDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RimsimRepository extends CommonRepository<MovMigra, String> {
   
   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_DynamicJoinStatement(:mod, :fields, :where)}", nativeQuery = true)
   List<Tuple> dynamicJoinStatement(@Param("mod") String mod, @Param("fields") String fields, @Param("where") String where);

   @Query(value = "{CALL dbo.usp_Rim_Utilitario_Dnv(:nacionalidad, :dependencia, :tipoMov, :fecIniMovMig, :fecFinMovMig)}", nativeQuery = true)
   List<Tuple> findDnvByParams(
      @Param("nacionalidad") String nacionalidad, 
      @Param("dependencia") String dependencia, 
      @Param("tipoMov") String tipoMov, 
      @Param("fecIniMovMig") String fecIniMovMig, 
      @Param("fecFinMovMig") String fecFinMovMig);

      @Query(value = "SELECT * FROM SimPais", nativeQuery = true)
      List<Pais> findAllPais();
   
      @Query(value = "SELECT sLogin, sNombre, sSiglas, sIdDependencia, sDni, sIdDocumento FROM SimUsuario", nativeQuery = true)
      List<Tuple> findAllSimUsuario();
   
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