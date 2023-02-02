package com.microservicio.rrhh.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rrhh.models.entities.FormatoPermisos;
import com.microservicio.rrhh.models.enums.AttachmentType;
import com.microservicio.rrhh.models.enums.ValidateType;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface RrhhService {

   // » Repo method's ...
   List<FormatoPermisos> findAllFormatoPermisos();
   void saveFormatoPermisos(FormatoPermisos formatoPermisos);
   FormatoPermisos findFormatoPermisosById(Long idFormato);
   void deleteFormatoPermisosById(Long idFormato);
   List<FormatoPermisos> findFormatoPermisosByUsrCreador(Usuario usrCreador);
   void validateFormatoPermisos(Long idFormato, ValidateType type);
   Long saveAllControlAsistencia(MultipartFile file) throws IOException;
   Long deleteAllControlAsistencia();
   List<Map<String, Object>> findAllControlAsistenciaUsrs();
   Long countControlAsistencias();
   List<Map<String, Object>> findControlPermisosByServidor(String servidor);
   void saveObservacionesFormatoPermisos(String observaciones, Long idFormato);
   void saveAttachment(MultipartFile file, AttachmentType type, Long idFormato) throws IOException;
   Resource findAttachmentById(Long idFormato, AttachmentType type);

   // » Other method's
   ByteArrayResource convertPermisosTemplateToByteArrResource(Long idFormato);

}
