package com.microservicio.generic.controllers;

import com.commons.utils.constants.Messages;
import com.commons.utils.utils.Response;
import com.microservicio.generic.services.DependenciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = { "*" })
@RestController
public class DependenciaController {

   @Autowired
   private DependenciaService service;

   @GetMapping(path = { "/findAllDependencia" })
   public ResponseEntity<?> findAllDependencia() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }

}