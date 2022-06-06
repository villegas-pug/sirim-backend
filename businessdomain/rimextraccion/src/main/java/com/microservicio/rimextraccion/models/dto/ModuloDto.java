package com.microservicio.rimextraccion.models.dto;

import java.util.Date;
import java.util.List;
import com.microservicio.rimextraccion.models.entities.BaseDatos;
import com.microservicio.rimextraccion.models.entities.QueryString;
import com.microservicio.rimextraccion.models.entities.Tabla;
import lombok.Data;

@Data
public class ModuloDto {
   private Long idMod;
   private BaseDatos baseDatos;
   private List<Tabla> tablas;
   private List<QueryString> queryStrings;
   private String nombre;
   private boolean activo;
   private Date fechaRegistro;

   /*► Are not a CLASS-POJO ...  */
   private QueryString queryString;
}
