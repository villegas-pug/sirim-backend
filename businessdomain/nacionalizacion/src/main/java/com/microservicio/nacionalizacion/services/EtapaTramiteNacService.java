package com.microservicio.nacionalizacion.services;

import java.util.Optional;

import com.commons.utils.models.entities.Etapa;
import com.microservicio.nacionalizacion.models.entities.EtapaTramiteNac;
import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;

public interface EtapaTramiteNacService {

   void save(EtapaTramiteNac etapaTramiteNac);
   Optional<EtapaTramiteNac> findById(Long idEtapaTramite);
   Optional<EtapaTramiteNac> findByEtapa(Etapa etapa);
   Optional<EtapaTramiteNac> findByTramiteNac(NuevoTramiteNac tramiteNac);
   Optional<EtapaTramiteNac> findByIdTramiteNacAndIdEtapa(Long idTramiteNac, Long idEtapa);
   
}