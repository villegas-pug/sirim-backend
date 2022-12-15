package com.commons.utils.models.dto;

import java.util.Date;

public interface RptProduccionHoraLaboralDto {

   Date getFechaAnalisis();
   int getHoraAnalisis();
   String getIdUsuario();
   String getNombres();
   String getGrupo();
   String getBase();
   String getEventos();
   int getTotalAnalizados();
   
}