package com.microservicio.nacionalizacion.models.dto;

public interface NacionalizacionRptDto {
   int getNro();
   String getAñoTramite();
   String getTipoTramite();
   String getEtapaTramite();
   int getContarPendiente();
}