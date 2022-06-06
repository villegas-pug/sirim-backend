package com.microservicio.nacionalizacion.services;

import java.util.List;

import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;

public interface UbicacionExpedienteService {

   List<UbicacionExpediente> findAll();
   
}