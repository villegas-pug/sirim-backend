package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Tuple;

import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.TablaDinamica;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.services.CommonService;

public interface RimextraccionService extends CommonService<TablaDinamica> {
   
   // ► Repo method's ...
   List<Tuple> createTable(String nombreTabla);
   Optional<TablaDinamica> findByNombre(String nombre);
   List<Map<String, Object>> findMetaTablaDinamicaByNombre(String nombreTabla);
   Long saveTablaDinamica(String nombreTabla, String sqlInsertValues);
   Long alterTablaDinamica(String queryString);
   List<Tuple> findAllTest();

   // ► Client-Methods ...
   List<Map<String, String>> findTableMetaByNameSim(String nombreTabla);
   List<Map<String, Object>> dynamicJoinStatementSim(QueryClauseDto queryClauseDto);
   List<TablaDinamicaDto> findAllTablaDinamica();
   List<TablaDinamicaDto> findTablaDinamicaByUsrCreador(Usuario usrCreador);

}