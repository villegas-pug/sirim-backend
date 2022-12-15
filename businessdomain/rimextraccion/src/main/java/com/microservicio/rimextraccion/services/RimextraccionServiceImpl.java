package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Tuple;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.TablaDinamica;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicio.rimextraccion.clients.RimcommonClientRest;
import com.microservicio.rimextraccion.clients.RimsimClientRest;
import com.microservicio.rimextraccion.repository.RimextraccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimextraccionServiceImpl extends CommonServiceImpl<TablaDinamica, RimextraccionRepository> implements RimextraccionService {

   @Autowired
   private RimsimClientRest rimsimRestClient;

   @Autowired
   private RimcommonClientRest rimcommonClient;

   @Override
   @Transactional
   public List<Tuple> createTable(String nombreTabla) {
      return super.repository.createTable(nombreTabla);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<TablaDinamica> findByNombre(String nombre) {
      return super.repository.findByNombre(nombre);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findMetaTablaDinamicaByNombre(String nombreTabla) {
      return DataModelHelper.convertTuplesToJson(super.repository.findMetaTablaDinamicaByNombre(nombreTabla), false);
   }

   @Override
   @Transactional
   public Long saveTablaDinamica(String nombreTabla, String sqlInsertValues) {
      return super.repository.saveTablaDinamica(nombreTabla, sqlInsertValues);
   }

   @Override
   @Transactional
   public Long alterTablaDinamica(String queryString) {
      return super.repository.alterTablaDinamica(queryString);
   }

   
   @Override
   public List<Tuple> findAllTest() {
      return this.repository.findAllTest();
   }
   
   //#region Client-Rest method's ...
   
   @Override
   public List<Map<String, String>> findTableMetaByNameSim(String nombreTabla) {
      return this.rimsimRestClient.findTableMetaByNameSim(nombreTabla);
   }

   @Override
   public List<Map<String, Object>> dynamicJoinStatementSim(QueryClauseDto queryClauseDto) {
      return this.rimsimRestClient.dynamicJoinStatementSim(queryClauseDto);
   }
   
   @Override
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonClient.findAllTablaDinamica().getData();
      if (tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return tablaDinamicaDb;
   }

   @Override
   public List<TablaDinamicaDto> findTablaDinamicaByUsrCreador(Usuario usrCreador) {
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonClient.findTablaDinamicaByUsrCreador(usrCreador).getData();
      if (tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return tablaDinamicaDb;
   }

   //#endregion



}