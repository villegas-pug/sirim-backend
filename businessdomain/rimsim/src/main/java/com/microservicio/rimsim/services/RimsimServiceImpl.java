package com.microservicio.rimsim.services;

import java.util.List;
import java.util.Map;

import com.commons.utils.helpers.DataModelHelper;
import com.microservicio.rimsim.models.repositories.RimsimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimsimServiceImpl implements RimsimService {

   @Autowired
   private RimsimRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, String>> findTableMetaByName(String nombreTabla) {
      return DataModelHelper.convertTuplesToFieldMetadata(this.repository.findTableMetaByName(nombreTabla));
   }

   @Override
   @Transactional(readOnly = true)
   public List<Object[]> dynamicJoinStatement(String mod, String fields, String where) {
      return this.repository.dynamicJoinStatement(mod, fields, where);
   }
   
}
