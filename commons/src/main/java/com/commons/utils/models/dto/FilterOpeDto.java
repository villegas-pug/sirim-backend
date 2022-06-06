package com.commons.utils.models.dto;

import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Empresa;
import lombok.Data;

@Data
public class FilterOpeDto {
   private Long idOpe;
   private String fecIni;
   private String fecFin;
   private Dependencia dependencia = new Dependencia();
   private String modalidad;
   private String sexo;
   private Empresa tipoOperativo = new Empresa();

}
