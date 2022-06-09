package com.microservicio.generic.controllers;

import com.commons.utils.constants.Messages;
import com.commons.utils.models.entities.Pais;
import com.commons.utils.utils.Response;
import com.microservicio.generic.services.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
public class PaisController {

   @Autowired
   private PaisService service;

   @GetMapping( path = { "/findAllPais" } )
   public ResponseEntity<?> findAllPais() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }
   

   @GetMapping(path = "/findByNacionalidad/{nacionalidad}")
   public Pais findByNacionalidad(@PathVariable String nacionalidad) {
      Pais paisDb = this.service
                           .findByNacionalidad(nacionalidad)
                           .orElse(this.service
                                          .findAll()
                                          .stream()
                                          .filter(pais -> pais.getIdPais().equals("VEN"))
                                          .findFirst()
                                          .get());
      return paisDb;
   }

}
