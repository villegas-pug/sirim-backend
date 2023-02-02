package com.microservicio.rrhh.models.dto;

import java.util.Date;

public interface ControlPermisosDto {

   Long getIdUsuario();
	String getServidor();
	Date getFechaControl();
	String getHoraEntrada();
	String getHoraSalida();
	String getTipoLicencia();
	Date getDesde();
	Date getHasta();
	String getTotalHoras();
	String getJustificacion();

}