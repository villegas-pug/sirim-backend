package com.microservicio.nacionalizacion.services;

import java.util.List;

import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;
import com.microservicio.nacionalizacion.models.repository.UbicacionExpedienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UbicacionExpedienteServiceImpl implements UbicacionExpedienteService {

   @Autowired
   private UbicacionExpedienteRepository repository;

   @Transactional(readOnly = true)
   @Override
   public List<UbicacionExpediente> findAll() {
      return this.repository.findAll();
   }
   
}