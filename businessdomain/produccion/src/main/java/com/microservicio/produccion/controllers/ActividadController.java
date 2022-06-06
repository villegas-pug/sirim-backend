package com.microservicio.produccion.controllers;

import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicio.produccion.models.entities.Actividad;
import com.microservicio.produccion.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = { "*" })
@RestController
public class ActividadController {
   
   @Autowired
   private ActividadService service;

   @GetMapping(path = "/findAllActividad")
   public ResponseEntity<?> findAllActividad() {
      List<Actividad> actividadDb = this.service.findAll();
      if(actividadDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(actividadDb)
            .build()
      );
   }

   @PostMapping(path ="/saveActividad")
   public ResponseEntity<?> saveActividad(@RequestBody Actividad actividad) {
      this.service.save(actividad);
      List<Actividad> actividadDb = this.service.findAll();
      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(actividadDb)
            .build()
      );
   }

   @DeleteMapping(path = "/deleteByIdActividad/{id}")
   public ResponseEntity<?> deleteByIdActividad(@PathVariable Long id){
      this.service.deleteById(id);
      List<Actividad> actividadDb = this.service.findAll();
      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(id))
            .data(actividadDb)
            .build()
      );
   }
}