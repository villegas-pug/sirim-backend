package com.microservicios.operativo.models.repository;

import java.util.List;
import com.commons.utils.models.entities.Refugiado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefugiadoRepository extends JpaRepository<Refugiado, Long> {

   @Query(
      value = "SELECT * FROM SidRefugiado WHERE sNombres LIKE :nombres% AND sPaterno LIKE :paterno% AND sMaterno LIKE :materno%", 
      nativeQuery = true)
   List<Refugiado> findByCustomFilter(
      @Param("nombres") String nombres,
      @Param("paterno") String paterno,
      @Param("materno") String materno);
}
