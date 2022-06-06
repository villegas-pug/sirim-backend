package com.microservicio.nacionalizacion.services;

import java.util.List;
import com.commons.utils.models.dto.IndicadoresCeremoniaDto;
import com.microservicio.nacionalizacion.models.entities.Ceremonia;

public interface CeremoniaService {

   List<Ceremonia> findAll();
   List<Ceremonia> saveAll(List<Ceremonia> ceremonia);
   List<IndicadoresCeremoniaDto> getSumarizzeCeremonia();
   
}