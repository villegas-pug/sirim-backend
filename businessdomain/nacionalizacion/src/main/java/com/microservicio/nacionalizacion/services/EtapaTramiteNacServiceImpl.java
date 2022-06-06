package com.microservicio.nacionalizacion.services;

import java.util.Optional;

import com.commons.utils.models.entities.Etapa;
import com.microservicio.nacionalizacion.models.entities.EtapaTramiteNac;
import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;
import com.microservicio.nacionalizacion.models.repository.EtapaTramiteNacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EtapaTramiteNacServiceImpl implements EtapaTramiteNacService {

   @Autowired
   private EtapaTramiteNacRepository  repository;

   @Override
   @Transactional
   public void save(EtapaTramiteNac etapaTramiteNac) {
      this.repository.save(etapaTramiteNac);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<EtapaTramiteNac> findById(Long idEtapaTramite) {
      return this.repository.findById(idEtapaTramite);
   }
   
   @Override
   @Transactional(readOnly = true)
   public Optional<EtapaTramiteNac> findByEtapa(Etapa etapa) {
      return this.repository.findByEtapa(etapa);
   }
   
   @Override
   @Transactional(readOnly = true)
   public Optional<EtapaTramiteNac> findByTramiteNac(NuevoTramiteNac tramiteNac) {
      return this.repository.findByTramiteNac(tramiteNac);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<EtapaTramiteNac> findByIdTramiteNacAndIdEtapa(Long idTramiteNac, Long idEtapa) {
      return this.repository.findByIdTramiteNacAndIdEtapa(idTramiteNac, idEtapa);
   }

   
}