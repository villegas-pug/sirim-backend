package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import com.microservicio.generic.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.generic.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.generic.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.generic.models.dto.RptPasaportesPorAñosDto;
import com.microservicio.generic.repository.SimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimServiceImp implements SimService {

   @Autowired
   private SimRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Pais> findAll() {
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

}
