package com.microservicio.rrhh.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.RimHttpHeaders;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rrhh.models.entities.FormatoPermisos;
import com.microservicio.rrhh.services.FormatoPermisosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "*" })
@RestController
public class FormatoPermisosController {

   @Autowired
   private FormatoPermisosService service;

   @GetMapping(path = { "/findAllFormatoPermisos" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<FormatoPermisos>> findAllFormatoPermisos() {
      return Response
               .<List<FormatoPermisos>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.service.findAllFormatoPermisos())
               .build();
   }

   @PostMapping(path = { "/findFormatoPermisosByUsrCreador" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<FormatoPermisos>> findFormatoPermisosByUsrCreador(@RequestBody Usuario usrCreador) {
      return Response
               .<List<FormatoPermisos>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.service.findFormatoPermisosByUsrCreador(usrCreador))
               .build();
   }

   @PostMapping(value="/saveFormatoPermisos")
   @ResponseStatus(code = HttpStatus.OK)
   public Response<?> saveFormatoPermisos(@RequestBody FormatoPermisos formatoPermisos) {
      this.service.saveFormatoPermisos(formatoPermisos);
      return Response
               .builder()
               .message(Messages.MESSAGE_SUCCESS_CREATE)
               .data(List.of())
               .build();
   }

   @GetMapping(value="/downloadFormatoLicenciaById")
   public ResponseEntity<?> downloadFormatoLicenciaById(@RequestParam Long idFormato) {

      //» Dep's
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      String contentDisposition = "attachment; filename=\"Formato de Autorización %s %s.xlsx\"";

      //» Service dep's 
      FormatoPermisos formatoPermisos = this.service.findFormatoPermisosById(idFormato);
      String servidor = formatoPermisos.getNombres();
      Date fechaFormato = formatoPermisos.getFechaFormato();
      ByteArrayResource permisoTemplateToByteArrResource = this.service.convertPermisosTemplateToByteArrResource(idFormato);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, servidor, dateFormat.format(fechaFormato)));
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);

      return ResponseEntity
                  .ok()
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .headers(headers)
                  .body(permisoTemplateToByteArrResource);
   }

   @DeleteMapping(path= { "/deleteFormatoPermisosById" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> deleteFormatoPermisosById(@RequestParam Long idFormato) {
      this.service.deleteFormatoPermisosById(idFormato);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_DELETE_RECORD(idFormato))
               .data(List.of())
               .build();
   }

   @DeleteMapping(path= { "/validateFormatoPermisos" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> validateFormatoPermisos(@RequestParam Long idFormato) {
      this.service.validateFormatoPermisos(idFormato);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_VALIDATE_RECORD_CTRLCAL)
               .data(List.of())
               .build();
   }
   

}