package com.microservicio.rimextraccion.controllers;

import java.util.UUID;

import javax.validation.Valid;

import com.microservicio.rimextraccion.models.dto.TipoLogicoDto;
import com.microservicio.rimextraccion.models.entities.TipoLogico;
import com.microservicio.rimextraccion.services.RimdepurarExtraccionService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = { "*" })
@RestController
public class RimdepurarExtraccionController {
   
   @Autowired
   private RimdepurarExtraccionService rimdepurarService;

   @Autowired
   private ModelMapper modelMapper;

   @PostMapping(path = { "/generateLongitudTest" })
   public String generateLongitudTest(@Valid @RequestBody TipoLogicoDto tipoLogicoDto, BindingResult result) {

      String errors = "";

      if(result.hasErrors()){
         for (FieldError fieldErr : result.getFieldErrors()) {
            errors = errors.concat(fieldErr.getField()).concat(": ")
                           .concat(fieldErr.getDefaultMessage());
         }

         return errors;
      }

      // ► Save ...
      TipoLogico tipoLogico = modelMapper.map(tipoLogicoDto, TipoLogico.class);
      this.rimdepurarService.saveTipoLogico(tipoLogico);

      return errors;
   }
   
   @GetMapping(path = { "/findAllTipoLogico" })
   public ResponseEntity<?> findAllTipoLogico() {
       return ResponseEntity.ok(this.rimdepurarService.findAllTipoLogico());
   }
   

}
