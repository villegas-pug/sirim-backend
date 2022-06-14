package com.microservicio.generic.models.repository;

import java.util.List;
import com.commons.utils.models.entities.Dependencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DependenciaRepository extends JpaRepository<Dependencia, String> {


   @Query(value = "SELECT * FROM SimDependencia", nativeQuery = true)
   List<Dependencia> findAllDependencia();

   
}
