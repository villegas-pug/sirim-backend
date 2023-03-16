package com.microservicio.rimreportes.repository;

import java.util.List;
import com.microservicio.rimreportes.model.dto.RptAñosControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptDependenciaControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptEdadesControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptNacionalidadControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptProduccionDiariaDto;
import com.microservicio.rimreportes.model.dto.RptProyeccionAnalisisDto;
import com.microservicio.rimreportes.model.entities.TablaDinamica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RimreportesRepository extends JpaRepository<TablaDinamica, Long> {

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Control_Migratorio(?, ?)}", nativeQuery = true)
   List<RptControlMigratorioDto> getRptControlMigratorio(int año, String nacionalidad);

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Años_Control_Migratorio}", nativeQuery = true)
   List<RptAñosControlMigratorioDto> getRptAñosControlMigratorio();

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Dependencia_Control_Migratorio(?, ?)}", nativeQuery = true)
   List<RptDependenciaControlMigratorioDto> getRptDependenciaControlMigratorio(int año, String nacionalidad);

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Edades_Control_Migratorio(?, ?)}", nativeQuery = true)
   List<RptEdadesControlMigratorioDto> getRptEdadesControlMigratorio(int año, String nacionalidad);

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Nacionalidad_Control_Migratorio(?)}", nativeQuery = true)
   List<RptNacionalidadControlMigratorioDto> getRptNacionalidadControlMigratorio(int año);

   @Query(value = "{CALL dbo.usp_Rim_Rpt_Produccion_Diaria(?, ?)}", nativeQuery = true)
   List<RptProduccionDiariaDto> getRptProduccionDiaria(String fecIni, String fecFin);

   @Query(value = "{CALL dbo.usp_Rim_rpt_ProyeccionAnalisisMensual(?)}", nativeQuery = true)
   List<RptProyeccionAnalisisDto> getRptProyeccionAnalisis(int año);
   
}
