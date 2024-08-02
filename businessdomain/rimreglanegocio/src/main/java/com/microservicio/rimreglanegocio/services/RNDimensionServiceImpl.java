package com.microservicio.rimreglanegocio.services;

import java.util.List;

import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.RNDimension;
import com.microservicio.rimreglanegocio.repositories.RNDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RNDimensionServiceImpl implements RNDimensionService {

   @Autowired
   private RNDimensionRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<RNDimension> findAllRNDimension() {
      List<RNDimension> dimensionDb = this.repository.findAll();

      if (dimensionDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return dimensionDb;
   }
   
}
