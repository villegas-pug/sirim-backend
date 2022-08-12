package com.microservicio.rimextraccion.controllers;

import java.util.List;

import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.TablaDinamica;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.RimextraccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


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

      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigGrupoAnalisisService.findById(idAsigGrupo);
      if(asigGrupoCamposAnalisis == null)
         throw new DataAccessEmptyWarning();

      /*► Update and Save ...  */
      asigGrupoCamposAnalisis.setUsrAnalista(usrAnalistaNew);
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
