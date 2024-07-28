package com.microservicio.rimreglanegocio.models.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "of", buildMethodName = "get")
public class RNProcesoDto {


   private Long idProceso;
   private String nombre;
   private String descripcion;
   private Long totalRegCorrectos;
   private Long totalRegIncorrectos; 
   private Long totalRegistros; 
   private int totalReglas;
   private boolean activo;
   private Date fechaCreacion;

   
}