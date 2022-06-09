package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.models.entities.Empresa;
import com.microservicio.generic.models.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaServiceImp implements EmpresaService {

   @Autowired
   private EmpresaRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Empresa> findAll() {
      return this.repository.findAll();
   }

}