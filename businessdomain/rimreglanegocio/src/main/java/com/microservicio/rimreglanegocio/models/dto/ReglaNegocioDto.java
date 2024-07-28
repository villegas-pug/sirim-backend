package com.microservicio.rimreglanegocio.models.dto;

import java.util.Date;

import com.commons.utils.models.entities.RNDimension;
import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.models.entities.RNStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "of", buildMethodName = "get")
public class ReglaNegocioDto {

   private String idRN;
   private String tablas;
   private String campos;
   private RNProceso proceso;
   private RNDimension dimensionRegla;
   private RNStatus statusRegla;
   private Date fechaCreacion;
   private boolean activo;
   
}