package com.commons.utils.models.dto;

import java.util.List;
import com.commons.utils.models.entities.UsrProcedimiento;
import lombok.Data;

@Data
public class UsuarioDto {
   private String idUsuario;
   private String login;
   private String nombres;
   private String dni;
   private String cargo;
   private String area;
   private String regimenLaboral;
   private List<UsrProcedimiento> usrProcedimiento;
}
