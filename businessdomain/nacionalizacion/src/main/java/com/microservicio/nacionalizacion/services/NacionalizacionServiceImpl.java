package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicio.nacionalizacion.models.dto.NacionalizacionRptDto;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;
import com.microservicio.nacionalizacion.models.repository.NacionalizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NacionalizacionServiceImpl extends CommonServiceImpl<Nacionalizacion, NacionalizacionRepository> implements NacionalizacionService {

   @Override
   @Transactional(readOnly = true)
   public List<NacionalizacionRptDto> countProcPendiente() {
      return super.repository.countProcPendiente();
   }

   @Override
   @Transactional(readOnly = true)
   public List<Nacionalizacion> findProcByCustomFilter(String añoTramite, String tipoTramite, String etapaTramite) {
      return super.repository.findProcByCustomFilter(añoTramite, tipoTramite, etapaTramite);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<Nacionalizacion> findByNumeroTramite(String numeroTramite) {
      return super.repository.findByNumeroTramite(numeroTramite);
   }
}