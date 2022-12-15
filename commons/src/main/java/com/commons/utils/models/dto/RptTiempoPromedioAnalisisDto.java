package com.commons.utils.models.dto;

import java.util.Date;

public interface RptTiempoPromedioAnalisisDto {
   
   int getNro();
   String getUsrAnalista();
   String getGrupo();
   String getBase();
   Date getFechaAnalisis();
   int getTotalAsignados();
   int getTotalAnalizados();
   String getFechaHoraAnalisisAvg();
   String getFechaHoraAnalisisSum();

}
