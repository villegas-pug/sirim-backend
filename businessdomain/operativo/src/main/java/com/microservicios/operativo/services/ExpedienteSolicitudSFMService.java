package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.microservicios.operativo.models.dto.MetadataFilesExpedienteSolicitudDto;
import com.microservicios.operativo.models.entities.ExpedienteSolicitudSFM;

public interface ExpedienteSolicitudSFMService {

   public List<ExpedienteSolicitudSFM> findAll();

   public void save(ExpedienteSolicitudSFM expedienteSolicitudSFM);

   public void saveAll(List<ExpedienteSolicitudSFM> lstexpedienteSolicitudSFM);

   List<MetadataFilesExpedienteSolicitudDto> getMetadataFilesExpedienteSolicitud();

   Optional<ExpedienteSolicitudSFM> findByNumeroExpediente(String numeroExpediente);
   
}