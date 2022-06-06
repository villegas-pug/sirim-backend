package com.microservicio.nacionalizacion.services;

import java.util.List;

import com.commons.utils.models.entities.TipoTramite;
import com.microservicio.nacionalizacion.models.entities.RequisitoTipoTramite;
import com.microservicio.nacionalizacion.models.repository.RequisitoTipoTramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequisitoTipoTramiteServiceImpl implements RequisitoTipoTramiteService {

   @Autowired
   private RequisitoTipoTramiteRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<RequisitoTipoTramite> findByTipoTramite(TipoTramite tipoTramite) {
      return this.repository.findByTipoTramite(tipoTramite);
   }

   
}