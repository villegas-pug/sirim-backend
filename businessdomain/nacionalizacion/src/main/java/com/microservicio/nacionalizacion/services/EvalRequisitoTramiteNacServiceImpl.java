package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.EvalRequisitoTramiteNac;
import com.microservicio.nacionalizacion.models.repository.EvalRequisitoTramiteNacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvalRequisitoTramiteNacServiceImpl implements EvalRequisitoTramiteNacService {

   @Autowired
   private EvalRequisitoTramiteNacRepository repository;

   @Override
   @Transactional
   public void saveAll(List<EvalRequisitoTramiteNac> evalRequisitoTramiteNac) {
      this.repository.saveAll(evalRequisitoTramiteNac);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<EvalRequisitoTramiteNac> findById(Long idEvalReqTramite) {
      return this.repository.findById(idEvalReqTramite);
   }

   @Override
   @Transactional
   public void save(EvalRequisitoTramiteNac evalRequisitoTramiteNac) {
      this.repository.save(evalRequisitoTramiteNac);
   }
}