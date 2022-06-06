package com.microservicio.rimextraccion.services;

import java.util.Optional;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;

public interface GrupoCamposAnalisisService{

   Optional<GrupoCamposAnalisis> findById(Long idGrupo);

}