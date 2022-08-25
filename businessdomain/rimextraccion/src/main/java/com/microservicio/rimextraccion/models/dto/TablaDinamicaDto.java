package com.microservicio.rimextraccion.models.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class TablaDinamicaDto {
 
   private Long idTabla;

   @JsonIgnoreProperties(value = { "tablaDinamica" })
   private List<GrupoCamposAnalisisDto> lstGrupoCamposAnalisis;

   private String nombre;
   private String metaFieldsCsv;

   @JsonIgnoreProperties(value = { "usrProcedimiento" })
   private Usuario usrCreador;

   private Date fechaCreacion;
   private boolean activo;

   /*» Utility Properties ...  */
   private String camposCsv;
   private String alterTableType;

   @JsonIgnoreProperties(value = { "tablaDinamica" })
   private GrupoCamposAnalisisDto grupoCamposAnalisis;
      
}