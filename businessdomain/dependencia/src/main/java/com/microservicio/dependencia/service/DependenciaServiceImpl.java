package com.microservicio.dependencia.service;

import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicio.dependencia.models.repository.DependenciaRepository;
import org.springframework.stereotype.Service;

@Service
public class DependenciaServiceImpl extends CommonServiceImpl<Dependencia, DependenciaRepository> implements DependenciaService{
}