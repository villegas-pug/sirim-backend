package com.microservicio.rimcommon.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import com.commons.utils.models.entities.TablaDinamica;
import com.commons.utils.models.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RimcommonRepository extends JpaRepository<TablaDinamica, Long> {

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_DynamicSelectStatement(?)}", nativeQuery = true)
   List<Tuple> findDynamicSelectStatement(String queryString);
   
   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ListarTablaDinamicaPorSufijoDeCampo(?, ?)}", nativeQuery = true)
   List<Tuple> findTablaDinamicaBySuffixOfField(String nombreTabla, String suffix);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ListarTablaDinamicaPorRangos(?, ?, ?)}", nativeQuery = true)
   List<Tuple> findTablaDinamicaByRangoFromIds(long idAsigGrupo, Long rIni, Long rFin);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ContarRegistrosTabla(?)}", nativeQuery = true)
   Long countTablaByNombre(String nombreTabla);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_AlterTablaDinamica(?)}", nativeQuery = true)
   Long alterTablaDinamica(String queryString);

   List<TablaDinamica> findByUsrCreador(Usuario usrCreador);

   Optional<TablaDinamica> findByNombre(String nombre);
   
}
