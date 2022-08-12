package com.microservicio.rimextraccion.dto;

import java.util.Date;
import java.util.List;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import lombok.Data;

@Data
public class GrupoCamposAnalisisDto {
   private Long idGrupo;
   private TablaDinamica tablaDinamica;
   private List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisis;
   private String nombre;
   private String metaFieldsCsv;
   private Date fechaCreacion;
   private boolean activo;
}
