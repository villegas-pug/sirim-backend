package com.microservicios.operativo.services;

import java.util.List;
import com.commons.utils.models.entities.Refugiado;

public interface RefugiadoService {
   List<Refugiado> findByCustomFilter(String nombres, String paterno, String materno);
}
