package com.microservicios.operativo.services;

import java.util.List;
import com.microservicios.operativo.models.entities.PreInscripcion;
import com.microservicios.operativo.models.repository.PreInscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreInscripcionServiceImpl implements PreInscripcionService {

   @Autowired
   private PreInscripcionRepository repository; 

   @Override
   @Transactional(readOnly = true)
   public List<PreInscripcion> findByNombresOrDocumento(String nombres, String apePat, String apeMat, String nroDoc) {
      return this.repository.findByNombresOrDocumento(nombres, apePat, apeMat, nroDoc);
   }
   
}