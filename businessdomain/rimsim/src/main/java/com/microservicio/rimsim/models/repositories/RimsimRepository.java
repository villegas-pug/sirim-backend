package com.microservicio.rimsim.models.repositories;

import java.util.List;
import com.commons.utils.models.repositories.CommonRepository;
import com.microservicio.rimsim.models.entities.MovMigra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RimsimRepository extends CommonRepository<MovMigra, String> {
 
   
   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_DynamicJoinStatement(:mod, :fields, :where)}", nativeQuery = true)
   List<Object[]> dynamicJoinStatement(@Param("mod") String mod, @Param("fields") String fields, @Param("where") String where);

   /* List<Object> findObjects(); */
   
}
