package com.microservicio.dependencia.controllers;

import com.commons.utils.controllers.CommonController;
import com.commons.utils.models.entities.Dependencia;
import com.microservicio.dependencia.service.DependenciaService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" })
@RestController
public class DependenciaController extends CommonController<Dependencia, DependenciaService> {
   
}