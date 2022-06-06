package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;

import com.commons.utils.services.CommonService;
import com.microservicio.nacionalizacion.models.dto.NacionalizacionRptDto;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;

public interface NacionalizacionService extends CommonService<Nacionalizacion> {
   List<NacionalizacionRptDto> countProcPendiente();
   List<Nacionalizacion> findProcByCustomFilter(String añoTramite, String tipoTramite, String etapaTramite);
   Optional<Nacionalizacion> findByNumeroTramite(String numeroTramite);
}