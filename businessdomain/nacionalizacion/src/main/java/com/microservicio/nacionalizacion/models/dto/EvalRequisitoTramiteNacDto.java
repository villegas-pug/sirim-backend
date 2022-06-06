package com.microservicio.nacionalizacion.models.dto;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.nacionalizacion.models.entities.RequisitoTipoTramite;
import com.microservicio.nacionalizacion.models.enums.EstadoEtapa;
import java.util.Date;
import lombok.Data;

@Data
public class EvalRequisitoTramiteNacDto {

   private Long idEvalReqTramite;
   private RequisitoTipoTramite requisitoTipoTramite;
   private Date fechaHoraInicio;
   private Date fechaHoraFin;
   private EstadoEtapa estado;
   private String observaciones;

   private Usuario usrDesig;
   
}