package com.microservicio.rrhh.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import com.microservicio.rrhh.models.entities.ControlAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ControlAsistenciaRepository extends JpaRepository<ControlAsistencia, Long> {

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_SaveTablaDinamica(?, ?)}", nativeQuery = true)
   Long saveTablaDinamica(String nombreTabla, String sqlStatement);
   
   @Query(value = "SELECT nIdUsuario, sNombre FROM RrhhControlAsistencia GROUP BY nIdUsuario, sNombre ORDER BY nIdUsuario, sNombre", nativeQuery = true)
   List<Tuple> findAllControlAsistenciaUsrs();

   @Query(value = "{CALL dbo.usp_Rrhh_Utilitarios_ControlPermisos(:servidor)}", nativeQuery = true)
   List<Tuple> findControlPermisosByServidor(@Param("servidor") String servidor);

   @Query(value = "SELECT TOP 1 ca.* FROM RrhhControlAsistencia ca WHERE ca.sNombre = ?", nativeQuery = true)
   Optional<ControlAsistencia> findServidorByNombre(String nombre);
}