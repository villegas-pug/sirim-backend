package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.EvaluarTramiteNac;
import com.microservicio.nacionalizacion.models.repository.EvaluarTramiteNacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluarTramiteNacServiceImpl implements EvaluarTramiteNacService {

   @Autowired
   private EvaluarTramiteNacRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<EvaluarTramiteNac> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional
   public void save(EvaluarTramiteNac evaluarTramiteNac) {
      this.repository.save(evaluarTramiteNac);
   }

   @Override
   @Transactional
   public void deleteById(Long idVerifExp) {
      this.repository.deleteById(idVerifExp);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<EvaluarTramiteNac> findById(Long id) {
      return this.repository.findById(id);
   }
   
}