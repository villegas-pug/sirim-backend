package com.commons.utils.models.dto;

import java.util.Date;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Data;

@Data
public class RecordsBetweenDatesDto {
   
   private Long idAsigGrupo;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fecIni;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fecFin;

   // ► Aux-1: Propiedades para el reporte de `Administrador` ...
   private String nombreTabla;
   private Usuario usr;

   // ► Aux-2 ...
   @JsonProperty
   private boolean isAssignedTemplate;

}
