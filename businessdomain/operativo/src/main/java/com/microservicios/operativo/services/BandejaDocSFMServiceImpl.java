
package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.microservicios.operativo.models.entities.BandejaDocSFM;
import com.microservicios.operativo.models.repository.BandejaDocSFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BandejaDocSFMServiceImpl implements BandejaDocSFMService {

   @Autowired
   private BandejaDocSFMRepository repository;

   @Override
   @Transactional
   public void save(BandejaDocSFM bandejaDocSFM) {
      this.repository.save(bandejaDocSFM);
   }
   
   @Override
   @Transactional
   public void saveAll(List<BandejaDocSFM> lstBandejaDocSFM) {
      this.repository.saveAll(lstBandejaDocSFM);
   }

   @Override
   @Transactional(readOnly = true)
   public List<BandejaDocSFM> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<BandejaDocSFM> findById(Long id) {
      return this.repository.findById(id);
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      this.repository.deleteById(id);
   }
   
}