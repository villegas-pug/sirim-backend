package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;
import com.microservicio.nacionalizacion.models.repository.NuevoTramiteNacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NuevoTramiteNacServiceImpl implements NuevoTramiteNacService {

   @Autowired
   private NuevoTramiteNacRepository repository;

   @Override
   @Transactional
   public List<NuevoTramiteNac> saveAll(List<NuevoTramiteNac> lstNuevoTramiteNac) {
      return this.repository.saveAll(lstNuevoTramiteNac);
   }

   @Override
   @Transactional(readOnly = true)
   public List<NuevoTramiteNac> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<NuevoTramiteNac> findByNumeroTramite(String numeroTramite) {
      return this.repository.findByNumeroTramite(numeroTramite);
   }

   @Override
   @Transactional
   public NuevoTramiteNac save(NuevoTramiteNac nuevoTramiteNac) {
      return this.repository.save(nuevoTramiteNac);
   }
   
}
