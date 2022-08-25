package com.microservicio.rimextraccion.models.dto;

import com.commons.utils.models.entities.Usuario;

import lombok.Data;

@Data
public class RecordAssignedDto {
   private String nombreTable; 
   private Long id;
   private String values;
   private Usuario usrAnalista;
   private Long regAnalisisIni;
   private Long regAnalisisFin;
   // ► New ...
   private AsigGrupoCamposAnalisisDto asigGrupo;
   private Long idCtrlCal;
   private boolean revisado;
   private String observacionesCtrlCal;
   private String metaFieldIdErrorCsv;
}
