package com.microservicio.nacionalizacion.services;

import java.util.List;

import com.commons.utils.models.entities.TipoTramite;
import com.microservicio.nacionalizacion.models.entities.RequisitoTipoTramite;

public interface RequisitoTipoTramiteService {

   List<RequisitoTipoTramite> findByTipoTramite(TipoTramite tipoTramite);
   
}