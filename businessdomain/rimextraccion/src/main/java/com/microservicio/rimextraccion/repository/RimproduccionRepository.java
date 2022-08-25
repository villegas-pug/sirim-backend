package com.microservicio.rimextraccion.repository;

import java.util.LinkedList;
import java.util.UUID;
import com.microservicio.rimextraccion.models.dto.ReporteMensualProduccionDto;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RimproduccionRepository extends JpaRepository<ProduccionAnalisis, Long> {

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ReporteMensualProduccion(?, ?, ?)}", nativeQuery = true)
   LinkedList<ReporteMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year);
   
}