package com.microservicio.rimextraccion.services;

import java.util.Optional;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;
import com.microservicio.rimextraccion.repository.GrupoCamposAnalisisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrupoCamposAnalisisServiceImpl implements GrupoCamposAnalisisService {

   @Autowired
   private GrupoCamposAnalisisRepository repository;

   @Override
   @Transactional(readOnly = true)
   public Optional<GrupoCamposAnalisis> findById(Long idGrupo) {
      return this.repository.findById(idGrupo);
   }

   @Override
   @Transactional
   public void deleteGrupoCamposAnalisisbyId(Long idGrupo) {
      this.repository.deleteById(idGrupo);
   }



}