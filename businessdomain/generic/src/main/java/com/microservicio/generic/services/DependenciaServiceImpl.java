package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.models.entities.Dependencia;
import com.microservicio.generic.models.repository.DependenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DependenciaServiceImpl implements DependenciaService{

   @Autowired
   private DependenciaRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Dependencia> findAll() {
      return this.repository.findAll();
   }
}