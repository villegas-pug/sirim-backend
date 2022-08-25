package com.microservicio.rimextraccion.controllers;

import java.util.Date;
import java.util.List;

import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.errors.RimcommonWarningException;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "*" })
@RestController
public class RimasignarController {
   
   @Autowired
   private RimasigGrupoCamposAnalisisService rimasigGrupoAnalisisService;
   
   @Autowired
   private RimcommonService rimcommonService;

   @PutMapping( path = { "/reasignToGrupoAnalisis" } )
   public ResponseEntity<?> reasignToGrupoAnalisis(@RequestBody AsigGrupoCamposAnalisis reasign) {

      /*► Dep's ... */
      Long idAsigGrupo = reasign.getIdAsigGrupo();
      Usuario usrAnalistaNew = reasign.getUsrAnalista();

      // ► Repo dep's ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigGrupoAnalisisService.findById(idAsigGrupo);
      if(asigGrupoCamposAnalisis == null)
         throw new DataAccessEmptyWarning();

      // ► Validación: Si no existe usuario ...
      if(usrAnalistaNew.getIdUsuario() == null)
         throw new RimcommonWarningException(Messages.WARNING_USER_NOT_EXISTS);

      /*► Update and Save ...  */
      asigGrupoCamposAnalisis.setUsrAnalista(usrAnalistaNew);
      asigGrupoCamposAnalisis.setFechaAsignacion(new Date());
      this.rimasigGrupoAnalisisService.save(asigGrupoCamposAnalisis);

      /*► Data-Set ... */
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonService.findAllTablaDinamica();

      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.SUCCESS_SAVE_TO_ASSIGN_REVISOR(usrAnalistaNew.getNombres()))
                                 .data(tablaDinamicaDb)
                                 .build());
   }

}
