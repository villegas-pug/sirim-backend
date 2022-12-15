package com.commons.utils.models.dto;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.entities.BaseDatos;
import com.commons.utils.models.entities.QueryString;
import com.commons.utils.models.entities.Tabla;

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
