package com.microservicio.nacionalizacion.models.dto;

import java.util.Date;

import com.commons.utils.models.entities.Etapa;
import com.microservicio.nacionalizacion.models.enums.EstadoTramite;
import lombok.Data;

@Data
public class NuevoTramiteNacDto {

   private Long idTramiteNac;
   /* private EvaluarTramiteNacDto evaluarTramiteNac; */
   /* private List<EtapaTramiteNac> etapaTramiteNac; */
   private String numeroTramite;
   private Long idTipoTramite;
   private String tipoTramite;
   private Date fechaTramite;
   private Date fechaRecepcionMP;
   private String administrado;
   private String sexo;
   private EstadoTramite estadoTramite;
   private Etapa etapa;
   private String paisNacimiento;
   private String dependencia;
   private Date fechaAud;
}