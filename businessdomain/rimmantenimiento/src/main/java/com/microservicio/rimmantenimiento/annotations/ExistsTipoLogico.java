package com.microservicio.rimmantenimiento.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import com.microservicio.rimmantenimiento.processors.ExistsTipoLogicoProcessor;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsTipoLogicoProcessor.class)
public @interface ExistsTipoLogico {
   String message() default "¡El nombre para el tipo lógico ya existe!";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
