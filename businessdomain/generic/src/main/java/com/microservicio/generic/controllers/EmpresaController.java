package com.microservicio.generic.controllers;

import com.commons.utils.constants.Messages;
import com.commons.utils.utils.Response;
import com.microservicio.generic.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" })
@RestController
public class EmpresaController {

   
   @Autowired
   private EmpresaService service;

   @GetMapping(path = { "/findAllEmpresa" })
   public ResponseEntity<?> findAllEmpresa() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }

}