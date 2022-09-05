package com.microservicio.rimextraccion.processors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.microservicio.rimextraccion.annotations.ExistsTipoLogico;
import com.microservicio.rimextraccion.services.RimdepurarExtraccionService;

import org.springframework.beans.factory.annotation.Autowired;

public class ExistsTipoLogicoProcessor implements ConstraintValidator<ExistsTipoLogico, String> {

   @Autowired
   private RimdepurarExtraccionService depurarService;
   
   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      boolean exists = this.depurarService
                                 .findAllTipoLogico()
                                 .stream()
                                 .anyMatch(t -> t.getNombre().equals(value.trim()));
      return !exists;
   }
   
}
