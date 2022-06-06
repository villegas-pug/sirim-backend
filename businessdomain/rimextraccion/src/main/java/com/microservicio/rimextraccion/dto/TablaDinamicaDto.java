package com.microservicio.rimextraccion.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;

import lombok.Data;

@Data
public class TablaDinamicaDto {
 
   private Long idTabla;
   private List<GrupoCamposAnalisis> lstGrupoCamposAnalisis;
   private String nombre;
   private Usuario usrCreador;
   private Date fechaCreacion;
   private boolean activo;

   /*» Properties are not a CLASS-POJO ...  */
   private String camposCsv;
   private String alterTableType;
   private GrupoCamposAnalisis grupoCamposAnalisis;
   
   
}