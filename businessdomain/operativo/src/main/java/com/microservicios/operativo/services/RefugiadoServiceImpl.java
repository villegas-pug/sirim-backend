package com.microservicios.operativo.services;

import java.util.List;
import com.commons.utils.models.entities.Refugiado;
import com.microservicios.operativo.models.repository.RefugiadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefugiadoServiceImpl implements RefugiadoService {

   @Autowired
   private RefugiadoRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Refugiado> findByCustomFilter(String nombres, String paterno, String materno) {
      return this.repository.findByCustomFilter(nombres, paterno, materno);
   }
   
}