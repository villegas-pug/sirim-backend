package com.microservicio.rimextraccion.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.entities.GrupoCamposAnalisis;

import lombok.Data;

@Data
public class TablaDinamicaDto {
 
   private Long idTabla;
   private List<GrupoCamposAnalisisDto> lstGrupoCamposAnalisis;
   private String nombre;
   private String metaFieldsCsv;
   private Usuario usrCreador;
   private Date fechaCreacion;
   private boolean activo;

   /*» Utility Properties ...  */
   private String camposCsv;
   private String alterTableType;
   private GrupoCamposAnalisis grupoCamposAnalisis;
   
   
}