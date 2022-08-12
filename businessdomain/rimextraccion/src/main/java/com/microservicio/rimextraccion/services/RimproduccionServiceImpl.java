package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import com.commons.utils.helpers.DataModelHelper;
import com.microservicio.rimextraccion.models.repository.RimcommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RimproduccionServiceImpl implements RimproduccionService {

   @Autowired
   private RimcommonRepository commonRepository;

   @Override
   public List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString) {
      List<Map<String, Object>> recordsAnalisados = DataModelHelper.convertTuplesToJson(this.commonRepository.findDynamicSelectStatement(queryString), false);
      return recordsAnalisados;
   }
   
}
