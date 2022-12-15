package com.microservicio.rimanalisis.repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Tuple;

import com.commons.utils.models.dto.RptMensualProduccionDto;
import com.commons.utils.models.dto.RptProduccionHoraLaboralDto;
import com.commons.utils.models.dto.RptTiempoPromedioAnalisisDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduccionAnalisisRepository extends JpaRepository<ProduccionAnalisis, Long> {

   Optional<ProduccionAnalisis> findByAsigGrupoAndIdRegistroAnalisis(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis, Long idRegistroAnalisis);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ReporteMensualProduccion(?, ?, ?)}", nativeQuery = true)
   LinkedList<RptMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year);

   @Query(value = "{CALL dbo.usp_rpt_Tiempo_Promedio_Analisis(?, ?)}", nativeQuery = true)
   List<RptTiempoPromedioAnalisisDto> getRptTiempoPromedioAnalisisByParms(Date fecIni, Date fecFin);

   @Query(value = "{CALL dbo.usp_rpt_Produccion_Horas_Laborales_Por_Analista(?, ?)}", nativeQuery = true)
   List<RptProduccionHoraLaboralDto> getRptProduccionHorasLaboralesPorAnalista(Date fechaAnalisis, String grupo);

   @Query(value = "{CALL dbo.usp_Rim_Rpt_S10_DRCM_FR001_Produccion(?, ?, ?, ?, ?)}", nativeQuery = true)
   List<Tuple> getRptS10DRCMFR001Produccion(String nombreTabla, Date fecIni, Date fecFin, boolean isRoot, Long idAsig);
   
}