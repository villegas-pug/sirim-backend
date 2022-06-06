package com.microservicios.operativo.services;

import com.commons.utils.models.entities.AuditoriaConsultaData;
import com.microservicios.operativo.models.repository.AuditoriaConsultaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaConsultaDataServiceImpl implements AuditoriaConsultaDataService {

   @Autowired
   private AuditoriaConsultaDataRepository repository;

   @Override
   @Transactional
   public void save(AuditoriaConsultaData entity) {
      this.repository.save(entity);
   }
   
}