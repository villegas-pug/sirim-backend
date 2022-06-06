package com.microservicios.pais.services;

import java.util.List;
import java.util.Optional;
import com.commons.utils.models.entities.Pais;
import com.microservicios.pais.models.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaisServiceImp implements PaisService {

   @Autowired
   private PaisRepository repository;

   @Override
   @Transactional(readOnly = true)
   public Optional<Pais> findByNacionalidad(String nacionalidad) {
      return this.repository.findByNacionalidad(nacionalidad);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Pais> findAll() {
      return this.repository.findAll();
   }

}
