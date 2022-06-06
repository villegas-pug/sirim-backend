package com.microservicios.interpol.models.repository;

import java.util.List;

import com.commons.utils.models.dto.InterpolDto;
import com.microservicios.interpol.models.entity.Interpol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterpolRepository extends JpaRepository<Interpol, Long> {

   @Query(
      value = "SELECT TOP 9 * FROM SidInterpol WHERE sNombres LIKE %:nombres% AND sApellidos LIKE %:apellidos% AND ISNULL(sCedula, '') LIKE %:cedula% AND ISNULL(sPasaporte, '') LIKE %:pasaporte% ORDER BY dFechaEmision DESC", 
      nativeQuery = true)
   List<Interpol> findByAppox(@Param("nombres") String nombres, @Param("apellidos") String apellidos, @Param("cedula") String cedula, @Param("pasaporte") String pasaporte);

   @Query(value = "SELECT (i.sNombres)nombres, (i.dFechaEmision)fechaEmision FROM SidInterpol i", nativeQuery = true)
   List<InterpolDto> testFindAll();


}
