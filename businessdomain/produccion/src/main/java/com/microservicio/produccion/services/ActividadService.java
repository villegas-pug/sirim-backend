package com.microservicio.produccion.services;

import java.util.List;

import com.microservicio.produccion.models.entities.Actividad;

public interface ActividadService {

   List<Actividad> findAll();
   void deleteById(Long id);
   Actividad save(Actividad actividad);
   
}