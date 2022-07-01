package com.microservicio.rimextraccion.models.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RimextraccionRepository extends JpaRepository<TablaDinamica, Long> {
   
   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_NuevaTablaDinamica(?)}", nativeQuery = true)
   List<Tuple> createTable(String nombreTabla);

   Optional<TablaDinamica> findByNombre(String nombre);
   
   @Query(value = "SELECT COLUMN_NAME nombre, DATA_TYPE tipo FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?", nativeQuery = true)
   List<Tuple> findMetaTablaDinamicaByNombre(String nombreTabla);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ListarTablaDinamicaPorSufijoDeCampo(?, ?)}", nativeQuery = true)
   List<Tuple> findTablaDinamicaBySuffixOfField(String nombreTabla, String suffix);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_SaveTablaDinamica(?, ?)}", nativeQuery = true)
   Long saveTablaDinamica(String nombreTabla, String sqlInsertValues);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_AlterTablaDinamica(?)}", nativeQuery = true)
   Long alterTablaDinamica(String queryString);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ContarRegistrosTabla(?)}", nativeQuery = true)
   Long countRegistrosExtraccion(String nombreTabla);

   @Query(value = "SELECT * FROM SidDependencia", nativeQuery = true)
   List<Tuple> findAllTest();

}
