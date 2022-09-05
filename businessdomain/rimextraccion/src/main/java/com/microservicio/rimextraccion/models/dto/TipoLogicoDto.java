package com.microservicio.rimextraccion.models.dto;

import java.util.Date;
import com.commons.utils.models.enums.RimGrupo;
import com.microservicio.rimextraccion.annotations.ExistsTipoLogico;

import lombok.Data;

@Data
public class TipoLogicoDto {
   
   private Integer idTipo;

   @ExistsTipoLogico
   private String nombre;

   private RimGrupo grupo;
   private String valoresCsv;
   private int longitud;
   private boolean activo;
   private Date fechaRegistro;

}
