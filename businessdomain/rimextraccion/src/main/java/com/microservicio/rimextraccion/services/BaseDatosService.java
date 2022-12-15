package com.microservicio.rimextraccion.services;

import java.util.List;

import com.commons.utils.models.entities.BaseDatos;

public interface BaseDatosService {

   List<BaseDatos> findAll();
   
}