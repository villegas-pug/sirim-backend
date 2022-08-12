package com.microservicio.rimextraccion.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import lombok.Data;

@Data
public class AsigGrupoCamposAnalisisDto {
   private Long idAsigGrupo;
   private GrupoCamposAnalisis grupo;
   private List<ProduccionAnalisis> produccionAnalisis;
   private Usuario usrAnalista;
   private int regAnalisisIni;
   private int regAnalisisFin;
   private Date fechaAsignacion;

   /*► Aux prop's ... */
   private Long totalAsignados;
   private Long totalAnalizados;
   private Long totalPendientes;
}
