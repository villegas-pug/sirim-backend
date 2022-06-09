package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.models.entities.Dependencia;

public interface DependenciaService {

   List<Dependencia> findAll();
   
}