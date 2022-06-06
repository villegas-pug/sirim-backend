package com.microservicio.rimextraccion.services;

import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.repository.AsigGrupoCamposAnalisisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsigGrupoCamposAnalisisServiceImpl implements AsigGrupoCamposAnalisisService{

   @Autowired
   private AsigGrupoCamposAnalisisRepository repository;

   @Override
   @Transactional
   public void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis) {
      this.repository.save(asigGrupoCamposAnalisis);
   }

   @Override
   @Transactional
   public void deleteById(Long idAsign) {
      this.repository.deleteById(idAsign);
   }

}