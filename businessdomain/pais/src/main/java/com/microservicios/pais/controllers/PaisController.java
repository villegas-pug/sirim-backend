package com.microservicios.pais.controllers;

import com.commons.utils.constants.Messages;
import com.commons.utils.utils.Response;
import com.microservicios.pais.services.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RefreshScope
@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
public class PaisController {

   @Autowired
   private PaisService service;

   @GetMapping( path = { "/findAll" } )
   public ResponseEntity<?> findAll() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }
   

   @GetMapping(path = "/findByNacionalidad/{nacionalidad}")
   public ResponseEntity<?> findByNacionalidad(@PathVariable String nacionalidad) {
      /* Pais paisDb = this.service.findByNacionalidad(nacionalidad)
                                 .orElse(this.service.findById("").get());
      return ResponseEntity.ok().body(paisDb); */
      return null;
   }


}
