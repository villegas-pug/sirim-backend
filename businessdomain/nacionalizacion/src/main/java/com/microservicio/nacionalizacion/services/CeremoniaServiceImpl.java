package com.microservicio.nacionalizacion.services;

import java.util.List;
import com.commons.utils.models.dto.IndicadoresCeremoniaDto;
import com.microservicio.nacionalizacion.models.entities.Ceremonia;
import com.microservicio.nacionalizacion.models.repository.CeremoniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CeremoniaServiceImpl implements CeremoniaService {

   @Autowired
   private CeremoniaRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Ceremonia> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional
   public List<Ceremonia> saveAll(List<Ceremonia> ceremonia) {
      return this.repository.saveAll(ceremonia);
   }

   @Override
   @Transactional(readOnly = true)
   public List<IndicadoresCeremoniaDto> getSumarizzeCeremonia() {
      return this.repository.getSumarizzeCeremonia();
   }
   
}