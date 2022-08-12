package com.microservicio.rimextraccion.models.dto;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import lombok.Data;

@Data
public class RecordAssignedDto {
   private String nombreTable; 
   private Long id;
   private String values;
   private Usuario usrAnalista;
   private Long regAnalisisIni;
   private Long regAnalisisFin;
   private AsigGrupoCamposAnalisis asigGrupo;
}
