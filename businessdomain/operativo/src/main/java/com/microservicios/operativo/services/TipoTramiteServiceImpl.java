package com.microservicios.operativo.services;

import java.util.List;

import com.commons.utils.models.entities.TipoTramite;
import com.microservicios.operativo.models.repository.TipoTramiteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoTramiteServiceImpl implements TipoTramiteService {

   @Autowired TipoTramiteRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<TipoTramite> findAll() {
      return this.repository.findAll();
   }
   
}