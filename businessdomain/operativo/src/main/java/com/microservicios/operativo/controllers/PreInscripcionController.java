package com.microservicios.operativo.controllers;

import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.entities.PreInscripcion;
import com.microservicios.operativo.services.PreInscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = { "*" })
@RestController
public class PreInscripcionController {
   
   @Autowired
   private PreInscripcionService service;

   @PostMapping(path = "/findByNombresOrDocumento")
   public ResponseEntity<?> findByNombresOrDocumento(
                                    @RequestParam(required = false, defaultValue = "") String nombres,
                                    @RequestParam(required = false, defaultValue = "") String apePat,
                                    @RequestParam(required = false, defaultValue = "") String apeMat,
                                    @RequestParam(required = false, defaultValue = "") String nroDoc) {
       
      List<PreInscripcion> preInscripcionDb = this.service.findByNombresOrDocumento(nombres, apePat, apeMat, nroDoc);
      
      if(preInscripcionDb.isEmpty())
         throw new DataAccessEmptyWarning();
                                    
      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(preInscripcionDb)
            .build()
      );
   }
}