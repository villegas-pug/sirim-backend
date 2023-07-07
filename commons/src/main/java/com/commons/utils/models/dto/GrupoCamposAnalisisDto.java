package com.commons.utils.models.dto;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class GrupoCamposAnalisisDto {
   private Long idGrupo;

   @JsonIgnoreProperties(value = { "lstGrupoCamposAnalisis" }, allowSetters = true)
   private TablaDinamicaDto tablaDinamica;

   @JsonIgnoreProperties(value = { "grupo", "produccionAnalisis" }, allowSetters = true)
   private List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisis;

   private String nombre;
   private String metaFieldsCsv;
   private Date fechaCreacion;
   private boolean obligatorio;
   private boolean activo;
}

