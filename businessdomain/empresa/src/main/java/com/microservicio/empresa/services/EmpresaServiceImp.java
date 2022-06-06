package com.microservicio.empresa.services;

import com.commons.utils.models.entities.Empresa;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicio.empresa.models.repositiry.EmpresaRepository;

import org.springframework.stereotype.Service;

@Service
public class EmpresaServiceImp extends CommonServiceImpl<Empresa, EmpresaRepository> implements EmpresaService {

}