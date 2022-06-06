package com.microservicios.operativo.services;

import java.util.List;

import com.commons.utils.models.entities.TipoTramite;

public interface TipoTramiteService {

   List<TipoTramite> findAll();
   
}