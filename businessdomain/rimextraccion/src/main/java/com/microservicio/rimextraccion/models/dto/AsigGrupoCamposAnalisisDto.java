package com.microservicio.rimextraccion.models.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.microservicio.rimextraccion.models.entities.CtrlCalCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import lombok.Data;

@Data
public class AsigGrupoCamposAnalisisDto {

   private Long idAsigGrupo;

   private GrupoCamposAnalisis grupo;

   @JsonIgnoreProperties(value = { "asigGrupo" })
   private List<ProduccionAnalisis> produccionAnalisis;

   @JsonIgnoreProperties(value = { "usrProcedimiento" })
   private Usuario usrAnalista;

   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" })
   private List<CtrlCalCamposAnalisis> ctrlsCalCamposAnalisis;

   private int regAnalisisIni;
   private int regAnalisisFin;
   private boolean ctrlCalConforme;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   private Date fechaAsignacion;

   /* ► Aux prop's ... */
   private Long totalAsignados;
   private Long totalAnalizados;
   private Long totalPendientes;

   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" })
   private CtrlCalCamposAnalisis ctrlCalCamposAnalisis;
}
