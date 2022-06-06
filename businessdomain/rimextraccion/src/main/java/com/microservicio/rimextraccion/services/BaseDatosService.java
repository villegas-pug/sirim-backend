package com.microservicio.rimextraccion.services;

import java.util.List;
import com.microservicio.rimextraccion.models.entities.BaseDatos;

public interface BaseDatosService {

   List<BaseDatos> findAll();
   
}