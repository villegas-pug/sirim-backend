package com.microservicio.rimreglanegocio.services;

import java.util.List;
import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.models.entities.ReglaNegocio;

public interface ReglaNegocioService {
   
   List<ReglaNegocio> findReglasNegocioByProceso(RNProceso proceso);
   ReglaNegocio findReglaNegocioById(String idRN);
   Long createOneRegistroEjecucionScript(int idProceso, Long idRNControlCambio);

}
