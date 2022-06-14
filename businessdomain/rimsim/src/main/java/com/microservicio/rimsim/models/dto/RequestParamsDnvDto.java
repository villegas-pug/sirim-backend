package com.microservicio.rimsim.models.dto;

import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Pais;
import lombok.Data;

@Data
public class RequestParamsDnvDto {

   private Pais pais;
   private Dependencia dependencia;
   private String tipoMov;
   private String fecIni;
   private String fecFin;
   
}