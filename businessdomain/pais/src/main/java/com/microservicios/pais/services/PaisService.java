package com.microservicios.pais.services;

import java.util.List;
import java.util.Optional;
import com.commons.utils.models.entities.Pais;

public interface PaisService {

   List<Pais> findAll();
   Optional<Pais> findByNacionalidad(String nacionalidad);
}
