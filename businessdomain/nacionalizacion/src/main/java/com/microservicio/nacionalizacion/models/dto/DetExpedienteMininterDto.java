package com.microservicio.nacionalizacion.models.dto;

import java.util.Date;

public interface DetExpedienteMininterDto {
   Long getIdDetExpMininter();
   String getNumeroExpediente();
   String getAccionesRealizadas();
   Date getFechaOficio();
   Date getFechaRecepcion();
   Date getFechaRegistro();
   String getNumeroOficio();
   String getEstado();
   Date getFechaRespuesta();
   Date getFechaVencimiento();
   int getEstadoPlazo();
}