package com.microservicio.rimextraccion.models.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Data
public class RecordsBetweenDatesDto {
   
   private Long idAsigGrupo;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fecIni;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fecFin;

}