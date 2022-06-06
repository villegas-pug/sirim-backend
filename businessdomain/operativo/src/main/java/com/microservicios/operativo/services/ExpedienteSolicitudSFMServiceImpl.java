package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.microservicios.operativo.models.dto.MetadataFilesExpedienteSolicitudDto;
import com.microservicios.operativo.models.entities.ExpedienteSolicitudSFM;
import com.microservicios.operativo.models.repository.ExpedienteSolicitudSFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpedienteSolicitudSFMServiceImpl implements ExpedienteSolicitudSFMService {

   @Autowired
   private ExpedienteSolicitudSFMRepository repository;

   @Override
   @Transactional
   public void save(ExpedienteSolicitudSFM expedienteSolicitudSFM) {
      this.repository.save(expedienteSolicitudSFM);
   }
   
   @Override
   @Transactional
   public void saveAll(List<ExpedienteSolicitudSFM> lstexpedienteSolicitudSFM) {
      this.repository.saveAll(lstexpedienteSolicitudSFM);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ExpedienteSolicitudSFM> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional(readOnly = true)
   public List<MetadataFilesExpedienteSolicitudDto> getMetadataFilesExpedienteSolicitud() {
      return this.repository.getMetadataFilesExpedienteSolicitud();
   }

   @Override
   public Optional<ExpedienteSolicitudSFM> findByNumeroExpediente(String numeroExpediente) {
      return this.repository.findByNumeroExpediente(numeroExpediente);
   }
   
}