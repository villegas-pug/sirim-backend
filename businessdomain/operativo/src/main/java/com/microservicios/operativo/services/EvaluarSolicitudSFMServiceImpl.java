package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import com.commons.utils.models.entities.Etapa;
import com.microservicios.operativo.models.entities.EvaluarSolicitudSFM;
import com.microservicios.operativo.models.repository.EvaluarSolicitudSFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluarSolicitudSFMServiceImpl implements EvaluarSolicitudSFMService {

   @Autowired
   private EvaluarSolicitudSFMRepository repository;

   @Override
   @Transactional(readOnly = true)
   public Optional<EvaluarSolicitudSFM> findById(Long id) {
      return this.repository.findById(id);
   }

   @Override
   @Transactional(readOnly = true)
   public List<EvaluarSolicitudSFM> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional
   public void save(EvaluarSolicitudSFM evaluarSolicitudSFM) {
      this.repository.save(evaluarSolicitudSFM);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Etapa> findAllEtapa() {
      return this.repository.findAllEtapa();
   }

   
}