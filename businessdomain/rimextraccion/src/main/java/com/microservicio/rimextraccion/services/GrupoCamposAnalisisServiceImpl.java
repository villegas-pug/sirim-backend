package com.microservicio.rimextraccion.services;

import java.util.Optional;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.repository.GrupoCamposAnalisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoCamposAnalisisServiceImpl implements GrupoCamposAnalisisService {

   @Autowired
   private GrupoCamposAnalisisRepository repository;

   @Override
   public Optional<GrupoCamposAnalisis> findById(Long idGrupo) {
      return this.repository.findById(idGrupo);
   }



}