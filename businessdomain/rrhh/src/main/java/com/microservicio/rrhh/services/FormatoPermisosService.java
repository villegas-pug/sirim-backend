package com.microservicio.rrhh.services;

import java.util.List;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.rrhh.models.entities.FormatoPermisos;

import org.springframework.core.io.ByteArrayResource;

public interface FormatoPermisosService {

   // » Repo method's ...
   List<FormatoPermisos> findAllFormatoPermisos();
   void saveFormatoPermisos(FormatoPermisos formatoPermisos);
   FormatoPermisos findFormatoPermisosById(Long idFormato);
   void deleteFormatoPermisosById(Long idFormato);
   List<FormatoPermisos> findFormatoPermisosByUsrCreador(Usuario usrCreador);
   void validateFormatoPermisos(Long idFormato);

   // » Other method's
   ByteArrayResource convertPermisosTemplateToByteArrResource(Long idFormato);

}
