package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.commons.utils.models.entities.Etapa;
import com.microservicios.operativo.models.entities.EvaluarSolicitudSFM;

public interface EvaluarSolicitudSFMService {

   Optional<EvaluarSolicitudSFM> findById(Long id);
   List<EvaluarSolicitudSFM> findAll();
   void save(EvaluarSolicitudSFM evaluarSolicitudSFM);
   List<Etapa> findAllEtapa();
   
}
