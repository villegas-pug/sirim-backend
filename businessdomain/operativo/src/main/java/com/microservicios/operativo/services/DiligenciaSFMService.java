package com.microservicios.operativo.services;

import java.util.Optional;
import com.microservicios.operativo.models.entities.DiligenciaSFM;

public interface DiligenciaSFMService {

   Optional<DiligenciaSFM> findById(Long id);
   void deleteById(Long id);
   
}