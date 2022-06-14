package com.microservicio.rimsim.models.repositories;

import java.util.List;
import javax.persistence.Tuple;
import com.commons.utils.models.entities.MovMigra;
import com.commons.utils.models.repositories.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RimsimRepository extends CommonRepository<MovMigra, String> {
   
   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_DynamicJoinStatement(:mod, :fields, :where)}", nativeQuery = true)
   List<Object[]> dynamicJoinStatement(@Param("mod") String mod, @Param("fields") String fields, @Param("where") String where);

   @Query(value = "{CALL dbo.usp_Rim_Utilitario_Dnv(:nacionalidad, :dependencia, :tipoMov, :fecIniMovMig, :fecFinMovMig)}", nativeQuery = true)
   List<Tuple> findDnvByParams(
      @Param("nacionalidad") String nacionalidad, 
      @Param("dependencia") String dependencia, 
      @Param("tipoMov") String tipoMov, 
      @Param("fecIniMovMig") String fecIniMovMig, 
      @Param("fecFinMovMig") String fecFinMovMig);
   
}