package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;

public interface NuevoTramiteNacService {

   List<NuevoTramiteNac> saveAll(List<NuevoTramiteNac> lstNuevoTramiteNac);
   NuevoTramiteNac save(NuevoTramiteNac nuevoTramiteNac);
   List<NuevoTramiteNac> findAll();
   Optional<NuevoTramiteNac> findByNumeroTramite(String numeroTramite);
   
}
