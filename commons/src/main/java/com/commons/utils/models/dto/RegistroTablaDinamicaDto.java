package com.commons.utils.models.dto;
import com.commons.utils.models.entities.Usuario;
import lombok.Data;

@Data
public class RegistroTablaDinamicaDto {
   private String nombreTabla; 
   private Long id;
   private String values;
   private Usuario usrAnalista;
   private Long regAnalisisIni;
   private Long regAnalisisFin;
   
   // ► Aux-1 ...
   private AsigGrupoCamposAnalisisDto asigGrupo;
   private Long idCtrlCal;
   private boolean revisado;
   private String observacionesCtrlCal;
   private String metaFieldIdErrorCsv;
   private boolean hasFieldError;

   // ► Aux-2 ...
   private String nId;
   private String nro;
   private String dFecha_Creacion;

   // ► Aux-3 ...
   private String fechaAnalisis;
   private String analizado;

}
