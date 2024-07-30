package com.microservicio.rimreglanegocio.services;

import java.util.List;

import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.models.entities.ReglaNegocio;
import com.microservicio.rimreglanegocio.repositories.ReglaNegocioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReglaNegocioServiceImpl implements ReglaNegocioService {

   @Autowired
   private ReglaNegocioRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<ReglaNegocio> findReglasNegocioByProceso(RNProceso proceso) {
      if (proceso.getIdProceso() == null)
         throw new DataAccessEmptyWarning();
      
      List<ReglaNegocio> reglaNegociosDb = this.repository.findByProceso(proceso);
      if (reglaNegociosDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return reglaNegociosDb;
   }

   @Override
   @Transactional
   public Long createOneRegistroEjecucionScript(Long idRNControlCambio) {
      return this.repository.createOneRegistroEjecucionScript(idRNControlCambio);
   }
   
}
