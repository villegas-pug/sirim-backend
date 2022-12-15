package com.commons.utils.models.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.CtrlCalCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Data;

@Data
public class AsigGrupoCamposAnalisisDto {

   private Long idAsigGrupo;

   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" }, allowSetters = true)
   private GrupoCamposAnalisisDto grupo;

   @JsonIgnoreProperties(value = { "asigGrupo" }, allowSetters = true)
   private List<ProduccionAnalisis> produccionAnalisis;

   @JsonIgnoreProperties(value = { "usrProcedimiento", "foto" })
   private Usuario usrAnalista;

   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" }, allowSetters = true)
   private List<CtrlCalCamposAnalisis> ctrlsCalCamposAnalisis;

   private int regAnalisisIni;
   private int regAnalisisFin;
   private boolean ctrlCalConforme;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaAsignacion;

   /* ► Aux prop's ... */
   private Long totalAsignados;
   private Long totalAnalizados;
   private Long totalPendientes;

   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" }, allowSetters = true)
   private CtrlCalCamposAnalisis ctrlCalCamposAnalisis;
}
