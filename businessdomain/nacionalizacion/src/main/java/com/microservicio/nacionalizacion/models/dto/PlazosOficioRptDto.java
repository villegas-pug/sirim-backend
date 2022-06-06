package com.microservicio.nacionalizacion.models.dto;

import lombok.Data;

@Data
public class PlazosOficioRptDto {
   private Long dentroPlazoVenc;
   private Long proximoPlazoVenc;
   private Long fueraPlazoVenc;
}
