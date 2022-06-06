package com.microservicios.operativo.services;

import java.util.Optional;

import com.microservicios.operativo.models.entities.DiligenciaSFM;
import com.microservicios.operativo.models.repository.DiligenciaSFMRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiligenciaSFMServiceImpl implements DiligenciaSFMService {

   @Autowired
   private DiligenciaSFMRepository repository;

   @Override
   @Transactional
   public void deleteById(Long id) {
      this.repository.deleteById(id);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<DiligenciaSFM> findById(Long id) {
      return this.repository.findById(id);
   }
   
}