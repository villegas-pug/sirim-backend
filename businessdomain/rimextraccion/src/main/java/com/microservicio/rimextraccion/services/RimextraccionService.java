package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Tuple;

import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.services.CommonService;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;

public interface RimextraccionService extends CommonService<TablaDinamica> {
   
   List<Tuple> createTable(String nombreTabla);
   Optional<TablaDinamica> findByNombre(String nombre);
   List<Map<String, Object>> findMetaTablaDinamicaByNombre(String nombreTabla);
   List<Map<String, Object>> findTablaDinamicaBySuffixOfField(String nombreTabla, String suffix);
   Long saveTablaDinamica(String nombreTabla, String sqlInsertValues);
   Long alterTablaDinamica(String queryString);
   Long countRegistrosExtraccion(String nombreTabla);
   List<Tuple> findAllTest();

   /*► Client-Methods ...  */
   public List<Map<String, String>> findTableMetaByNameSim(String nombreTabla);
   public List<Object[]> dynamicJoinStatementSim(QueryClauseDto queryClauseDto);
}