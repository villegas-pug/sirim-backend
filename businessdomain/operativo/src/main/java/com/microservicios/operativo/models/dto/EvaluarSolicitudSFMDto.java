package com.microservicios.operativo.models.dto;

import com.microservicios.operativo.models.entities.DiligenciaSFM;
import lombok.Data;

@Data
public class EvaluarSolicitudSFMDto {

   private Long idVerifExp;
   private DiligenciaSFM diligencia;
   
}