package com.commons.utils.models.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Data
public class TablaDinamicaDto {
 
   private Long idTabla;

   @JsonIgnoreProperties(value = { "tablaDinamica" }, allowSetters = true)
   private List<GrupoCamposAnalisisDto> lstGrupoCamposAnalisis;

   private String nombre;
   private String metaFieldsCsv;

   @JsonIgnoreProperties(value = { "usrProcedimiento", "foto" }, allowSetters = true)
   private Usuario usrCreador;

   private int porcentajeQC;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaCreacion;
   private boolean activo;

   /*» Utility Properties ...  */
   private String camposCsv;
   private String alterTableType;

   @JsonIgnoreProperties(value = { "tablaDinamica" }, allowGetters = true)
   private GrupoCamposAnalisisDto grupoCamposAnalisis;
      
}