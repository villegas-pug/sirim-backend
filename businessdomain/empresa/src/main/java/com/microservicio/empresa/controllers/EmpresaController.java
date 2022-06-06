package com.microservicio.empresa.controllers;

import com.commons.utils.controllers.CommonController;
import com.commons.utils.models.entities.Empresa;
import com.microservicio.empresa.services.EmpresaService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@CrossOrigin(origins = { "*" })
@RestController
public class EmpresaController extends CommonController<Empresa, EmpresaService> {

}