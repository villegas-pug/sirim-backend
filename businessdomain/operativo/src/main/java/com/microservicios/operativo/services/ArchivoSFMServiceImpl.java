package com.microservicios.operativo.services;

import java.util.Optional;

import com.microservicios.operativo.models.entities.ArchivoSFM;
import com.microservicios.operativo.models.repository.ArchivoSFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArchivoSFMServiceImpl implements ArchivoSFMService {

   @Autowired
   private ArchivoSFMRepository repository;

   @Override
   @Transactional
   public void save(ArchivoSFM archivoSFM) {
      this.repository.save(archivoSFM);
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      this.repository.deleteById(id);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ArchivoSFM> findById(Long id) {
      return this.repository.findById(id);
   }
}