package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.EvalRequisitoTramiteNac;

public interface EvalRequisitoTramiteNacService {

   void saveAll(List<EvalRequisitoTramiteNac> evalRequisitoTramiteNac);
   void save(EvalRequisitoTramiteNac evalRequisitoTramiteNac);
   Optional<EvalRequisitoTramiteNac> findById(Long idEvalReqTramite);
   
}