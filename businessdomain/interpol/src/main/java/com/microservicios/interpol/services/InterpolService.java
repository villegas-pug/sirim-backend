package com.microservicios.interpol.services;

import java.util.List;

import com.commons.utils.models.dto.InterpolDto;
import com.commons.utils.services.CommonService;
import com.microservicios.interpol.models.entity.Interpol;

public interface InterpolService extends CommonService<Interpol> {

   List<Interpol> findByAppox(String nombres, String apellidos, String cedula, String pasaporte);
   List<InterpolDto> testFindAll();
   List<Interpol> saveAll(List<Interpol> interpol);
}
