package com.microservicio.rimextraccion.models.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import lombok.Data;

@Data
public class GrupoCamposAnalisisDto {
   private Long idGrupo;

   private TablaDinamica tablaDinamica;

   @JsonIgnoreProperties(value = { "grupo" })
   private List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisis;

   private String nombre;
   private String metaFieldsCsv;
   private Date fechaCreacion;
   private boolean activo;
}
