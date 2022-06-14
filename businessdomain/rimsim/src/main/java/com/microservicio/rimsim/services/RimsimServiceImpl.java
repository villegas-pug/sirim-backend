package com.microservicio.rimsim.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
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
   public List<Map<String, Object>> findTableMetaByName(String nombreTabla) {
      return DataModelHelper.convertTuplesToJson(this.repository.findTableMetaByName(nombreTabla), false);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Object[]> dynamicJoinStatement(String mod, String fields, String where) {
      return this.repository.dynamicJoinStatement(mod, fields, where);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Tuple> findDnvByParams(String nacionalidad, String dependencia, String tipoMov, String fecIniMovMig, String fecFinMovMig) {
      return this.repository.findDnvByParams(nacionalidad, dependencia, tipoMov, fecIniMovMig, fecFinMovMig);
   }
   
}
