package com.commons.utils.models.dto;

import java.util.List;
import java.util.UUID;

import com.commons.utils.models.entities.UsrProcedimiento;
import com.commons.utils.models.enums.RimGrupo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class UsuarioDto {

   private UUID idUsuario;
   private String nombres;

   @JsonIgnoreProperties(value = { "usuario" })
   private List<UsrProcedimiento> usrProcedimiento;

   private String login;
   private String password;
   private String dependencia;
   private String cargo;
   private RimGrupo grupo;
   private String area;
   private String dni;
   private String regimenLaboral;
   private String foto;

}
