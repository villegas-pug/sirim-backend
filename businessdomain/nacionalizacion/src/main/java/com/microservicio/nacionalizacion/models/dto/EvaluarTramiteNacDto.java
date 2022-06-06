package com.microservicio.nacionalizacion.models.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class EvaluarTramiteNacDto {

   private Long idVerifExp;
   private NuevoTramiteNacDto tramiteNac;
   private List<EvalRequisitoTramiteNacDto> evalRequisitoTramiteNac;
   private Date fechaRegistro;
   private Date fechaDerivacion;
   private Date fechaAprobacion;
   private boolean leido;
   private boolean activo;
   private boolean completado;

}