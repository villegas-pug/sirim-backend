package com.microservicio.generic.models.repository;

import java.util.Optional;
import com.commons.utils.models.entities.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends JpaRepository<Pais, String> {

   @Query(value = "SELECT TOP 1 * FROM SidPais sp WHERE sp.sNacionalidad LIKE %:nacionalidad%", nativeQuery = true)
   Optional<Pais> findByNacionalidad(@Param("nacionalidad") String nacionalidad);
}
