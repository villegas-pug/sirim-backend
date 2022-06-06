package com.microservicio.rimextraccion.services;

import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;

public interface AsigGrupoCamposAnalisisService{

   void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis);
   void deleteById(Long idAsign);

}