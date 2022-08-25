package com.microservicio.rimextraccion.models.dto;

import java.util.Date;

public interface ReporteMensualProduccionDto {

   String getSemanaProd();
   Date getFechaProd();
   String getNombreBase();
   Integer getTotalProd();
   String getObservaciones();
   
}