package com.microservicio.rimmantenimiento.processors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.microservicio.rimmantenimiento.annotations.ExistsTipoLogico;
import com.microservicio.rimmantenimiento.services.RimmantenimientoService;

import org.springframework.beans.factory.annotation.Autowired;

public class ExistsTipoLogicoProcessor implements ConstraintValidator<ExistsTipoLogico, String> {

   @Autowired
   private RimmantenimientoService rimmantenimientoService;
   
   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      boolean exists = this.rimmantenimientoService
                                    .findAllTipoLogico()
                                    .stream()
                                    .anyMatch(t -> t.getNombre().equals(value.trim()));
      return !exists;
   }
   
}
