package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.microservicios.operativo.models.entities.BandejaDocSFM;

public interface BandejaDocSFMService {

   void save(BandejaDocSFM bandejaDocSFM);

   void saveAll(List<BandejaDocSFM> lstBandejaDocSFM);

   List<BandejaDocSFM> findAll();

   Optional<BandejaDocSFM> findById(Long id);

   void deleteById(Long id);

   

}