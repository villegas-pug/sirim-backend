package com.microservicio.nacionalizacion.models.dto;

import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;

import lombok.Data;

@Data
public class ExpedienteMininterDto {
   private Nacionalizacion nacionalizacion;
   private ExpedienteMininter expedienteMininter;
}
