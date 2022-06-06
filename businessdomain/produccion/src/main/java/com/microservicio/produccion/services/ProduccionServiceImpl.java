package com.microservicio.produccion.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.commons.utils.models.dto.ProduccionDiariaDto;
import com.commons.utils.models.dto.ProduccionSemanalDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.services.CommonServiceImpl;
import com.microservicio.produccion.client.UsuarioClient;
import com.microservicio.produccion.models.entities.Produccion;
import com.microservicio.produccion.models.repository.ProduccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProduccionServiceImpl extends CommonServiceImpl<Produccion, ProduccionRepository> implements ProduccionService {

   @Autowired
   private UsuarioClient usuarioClient;

   @Override
   @Transactional(readOnly = true)
   public Optional<Usuario> findByUserAuth(String userAuth) {
      return usuarioClient.findByUserAuth(userAuth);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Produccion> findByUsuarioAndFechaRegistro(Usuario usuario, Date fechaRegistro) {
      return super.repository.findByUsuarioAndFechaRegistro(usuario, fechaRegistro);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ProduccionDiariaDto> countActividadCurrentWeek(String loginName) {
      return super.repository.countActividadCurrentWeek(loginName);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ProduccionSemanalDto> countActividadSemanalByDate(Date refDate) {
      return super.repository.countActividadSemanalByDate(refDate);
   }
      
}
