package com.microservicio.rimextraccion.models.repository;

import java.util.List;
import javax.persistence.Tuple;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RimcommonRepository extends JpaRepository<TablaDinamica, Long> {

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_DynamicSelectStatement(?)}", nativeQuery = true)
   List<Tuple> findDynamicSelectStatement(String queryString);
   
}
