package com.microservicios.operativo.services;

import java.util.Optional;

import com.microservicios.operativo.models.entities.ArchivoSFM;

public interface ArchivoSFMService {

   void save(ArchivoSFM archivoSFM);
   void deleteById(Long id);
   Optional<ArchivoSFM> findById(Long id);
   
}