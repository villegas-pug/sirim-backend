package com.microservicio.generic.services;

import java.util.List;
import com.commons.utils.models.entities.Empresa;

public interface EmpresaService {

   List<Empresa> findAll();

}