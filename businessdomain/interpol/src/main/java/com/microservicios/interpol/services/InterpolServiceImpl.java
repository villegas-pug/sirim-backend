package com.microservicios.interpol.services;

import java.util.List;

import com.commons.utils.models.dto.InterpolDto;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicios.interpol.models.entity.Interpol;
import com.microservicios.interpol.models.repository.InterpolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterpolServiceImpl extends CommonServiceImpl<Interpol, InterpolRepository> implements InterpolService {

   @Override
   @Transactional(readOnly = true)
   public List<Interpol> findByAppox(String nombres, String apellidos) {
      return super.repository.findByAppox(nombres, apellidos);
   }

   @Override
   @Transactional(readOnly = true)
   public List<InterpolDto> testFindAll() {
      return super.repository.testFindAll();
   }

   @Transactional
   @Override
   public List<Interpol> saveAll(List<Interpol> interpol) {
      return super.repository.saveAll(interpol);
   }

}