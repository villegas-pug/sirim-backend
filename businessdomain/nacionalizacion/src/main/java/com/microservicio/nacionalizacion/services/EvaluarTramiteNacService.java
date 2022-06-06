package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.EvaluarTramiteNac;

public interface EvaluarTramiteNacService {

   List<EvaluarTramiteNac> findAll();
   Optional<EvaluarTramiteNac> findById(Long id);
   void save(EvaluarTramiteNac evaluarTramiteNac);
   void deleteById(Long idVerifExp);
   
}