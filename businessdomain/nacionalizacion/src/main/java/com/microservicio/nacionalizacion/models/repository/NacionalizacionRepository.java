package com.microservicio.nacionalizacion.models.repository;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.dto.NacionalizacionRptDto;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NacionalizacionRepository extends JpaRepository<Nacionalizacion, Long> {
 
   @Query(value = "{CALL dbo.usp_Sid_Rpt_ContarProcPendiente}", nativeQuery = true)
   List<NacionalizacionRptDto> countProcPendiente();

   @Query(value = "{CALL dbo.usp_Sid_Rpt_BuscarProcPorFiltro(:añoTramite, :tipoTramite, :etapaTramite)}", nativeQuery = true)
   List<Nacionalizacion> findProcByCustomFilter(String añoTramite, String tipoTramite, String etapaTramite);
 
   Optional<Nacionalizacion> findByNumeroTramite(String numeroTramite);
   
}