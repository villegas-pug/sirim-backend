package com.microservicio.produccion.services;

import java.util.List;
import com.microservicio.produccion.models.entities.Actividad;
import com.microservicio.produccion.models.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActividadServiceImpl implements ActividadService {

   @Autowired
   private ActividadRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Actividad> findAll() {
      return this.repository.findAll(Sort.by(Sort.Direction.DESC, "fechaAuditoria"));
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      this.repository.deleteById(id);
   }

   @Override
   public Actividad save(Actividad actividad) {
      return this.repository.save(actividad);
   }
    
}