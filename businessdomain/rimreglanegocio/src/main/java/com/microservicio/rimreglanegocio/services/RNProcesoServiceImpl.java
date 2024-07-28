package com.microservicio.rimreglanegocio.services;

import java.util.List;

import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.RNProceso;
import com.microservicio.rimreglanegocio.repositories.RNProcesoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RNProcesoServiceImpl implements RNProcesoService {

   @Autowired
   private RNProcesoRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<RNProceso> findAllRNProceso() {
      List<RNProceso> rnProcesoDb = this.repository.findAll();
      if (rnProcesoDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return rnProcesoDb;
   }
   
}
