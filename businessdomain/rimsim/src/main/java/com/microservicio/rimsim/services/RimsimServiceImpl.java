package com.microservicio.rimsim.services;

import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import com.microservicio.rimsim.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.rimsim.models.dto.RptPasaportesPorAñosDto;
import com.microservicio.rimsim.models.repositories.RimsimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimsimServiceImpl implements RimsimService {

   @Autowired
   private RimsimRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findTableMetaByName(String nombreTabla) {
      return DataModelHelper.convertTuplesToJson(this.repository.findTableMetaByName(nombreTabla), false);
   }

   @Override
   @Transactional(readOnly = true)
   @Cacheable(cacheNames = { "extraccion" })
   public List<Map<String, Object>> dynamicJoinStatement(String mod, String fields, String where) {
      return DataModelHelper.convertTuplesToJson(this.repository.dynamicJoinStatement(mod, fields, where), false);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Tuple> findDnvByParams(String nacionalidad, String dependencia, String tipoMov, String fecIniMovMig, String fecFinMovMig) {
      return this.repository.findDnvByParams(nacionalidad, dependencia, tipoMov, fecIniMovMig, fecFinMovMig);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Pais> findAllPais() {
      return this.repository.findAllPais();
   }

   @Override
   @Transactional(readOnly = true)
   public List<Dependencia> findAllDependencia() {
      return this.repository.findAllDependencia();
   }

   @Override
   @Transactional(readOnly = true)
   public RptPasaportesIndicadoresDto getRptPasaportesIndicadores() {
      RptPasaportesIndicadoresDto rptPasaportesIndicadores = this.repository
                                                                     .getRptPasaportesIndicadores()
                                                                     .orElseThrow(DataAccessEmptyWarning::new);
      return rptPasaportesIndicadores;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptPasaportesPorAñosDto> getRptPasaportesEntregadosPorAños() {
      List<RptPasaportesPorAñosDto> rptPasaportesPorAños = this.repository.getRptPasaportesEntregadosPorAños();
      if (rptPasaportesPorAños.size() == 0) throw new DataAccessEmptyWarning();
      return rptPasaportesPorAños;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptPasaportesPor12UltimosMesesDto> getRptPasaportesEntregadosPor12UltimosMeses() {
      List<RptPasaportesPor12UltimosMesesDto> rptPasaportesPor12UltimosMeses = this.repository.getRptPasaportesEntregadosPor12UltimosMeses();
      if (rptPasaportesPor12UltimosMeses.size() == 0) throw new DataAccessEmptyWarning();
      return rptPasaportesPor12UltimosMeses;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptPasaportesPor31UltimosDiasDto> getRptPasaportesEntregadosPor31UltimosDias() {
      List<RptPasaportesPor31UltimosDiasDto> rptPasaportesPor31UltimosDias = this.repository.getRptPasaportesEntregadosPor31UltimosDias();
      if (rptPasaportesPor31UltimosDias.size() == 0) throw new DataAccessEmptyWarning();
      return rptPasaportesPor31UltimosDias;
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findAllSimUsuario() {
      List<Map<String, Object>> simUsuarioDb = DataModelHelper.convertTuplesToJson(this.repository.findAllSimUsuario(), true);
      if (simUsuarioDb.size() == 0) throw new DataAccessEmptyWarning();
      return simUsuarioDb;
   }
   
}
