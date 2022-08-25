package com.microservicio.rimextraccion.controllers;

import java.util.List;
import java.util.Map;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.RimextraccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" })
@RestController
public class RimcommonController {
   
   @Autowired
   private RimextraccionService rimextraccionService;

   @Autowired
   private RimcommonService rimcommonService;

   @GetMapping( path = { "/findAllTablaDinamica" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> findAllTablaDinamica() {

      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonService.findAllTablaDinamica();
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();

      return Response
               .<List<TablaDinamicaDto>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(tablaDinamicaDb)
               .build();
   }

   @GetMapping( path = { "/findTablaDinamicaBySuffixOfField" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findTablaDinamicaBySuffixOfField(@RequestParam String nombreTabla, @RequestParam(defaultValue = "") String suffix){
      List<Map<String, Object>> tablaDinamicaDb = this.rimextraccionService.findTablaDinamicaBySuffixOfField(nombreTabla, suffix);
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(tablaDinamicaDb)
               .build();
   }

}
