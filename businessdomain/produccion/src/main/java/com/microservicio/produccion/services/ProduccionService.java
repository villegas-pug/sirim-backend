package com.microservicio.produccion.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.commons.utils.models.dto.ProduccionDiariaDto;
import com.commons.utils.models.dto.ProduccionSemanalDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.services.CommonService;
import com.microservicio.produccion.models.entities.Produccion;

public interface ProduccionService extends CommonService<Produccion> {

   Optional<Usuario> findByUserAuth(String userAuth);
   List<Produccion> findByUsuarioAndFechaRegistro(Usuario usuario, Date fechaRegistro);
   List<ProduccionDiariaDto> countActividadCurrentWeek(String loginName);
   List<ProduccionSemanalDto> countActividadSemanalByDate(Date refDate);
   
}
