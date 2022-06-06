package com.microservicios.operativo.services;

import java.util.List;
import com.microservicios.operativo.models.entities.PreInscripcion;

public interface PreInscripcionService {
   List<PreInscripcion> findByNombresOrDocumento(String nombres, String apePat,String apeMat,String nroDoc);
}
