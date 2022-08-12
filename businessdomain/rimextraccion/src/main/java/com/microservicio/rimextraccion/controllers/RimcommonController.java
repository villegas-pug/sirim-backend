package com.microservicio.rimextraccion.controllers;

import java.util.List;
import java.util.Map;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.RimextraccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" })
@RestController
public class RimcommonController {
   
   @Autowired
   private RimextraccionService rimextraccionService;

   @Autowired
   private RimcommonService rimcommonService;

   @GetMapping( path = { "/findAllTablaDinamica" } )
   public ResponseEntity<?> findAllTablaDinamica() {

      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonService.findAllTablaDinamica();
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(tablaDinamicaDb)
                                       .build());
   }

   @GetMapping( path = { "/findTablaDinamicaBySuffixOfField" } )
   public ResponseEntity<?> findTablaDinamicaBySuffixOfField(@RequestParam String nombreTabla, @RequestParam(defaultValue = "") String suffix){
      List<Map<String, Object>> tablaDinamicaDb = this.rimextraccionService.findTablaDinamicaBySuffixOfField(nombreTabla, suffix);
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(tablaDinamicaDb)
                                       .build());
   }

}
