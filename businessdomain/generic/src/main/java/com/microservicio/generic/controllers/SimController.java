package com.microservicio.generic.controllers;

import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.utils.Response;
import com.microservicio.generic.models.dto.RptPasaportesIndicadoresDto;
import com.microservicio.generic.models.dto.RptPasaportesPor12UltimosMesesDto;
import com.microservicio.generic.models.dto.RptPasaportesPor31UltimosDiasDto;
import com.microservicio.generic.models.dto.RptPasaportesPorAñosDto;
import com.microservicio.generic.services.SimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@CrossOrigin(origins = { "*" })
@RestController
public class SimController {

   @Autowired
   private SimService service;

   @GetMapping( path = { "/findAllPais" } )
   public ResponseEntity<?> findAllPais() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }

   @GetMapping(path = { "/findAllDependencia" })
   public ResponseEntity<?> findAllDependencia() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.service.findAll())
                                       .build());
   }

   @GetMapping( path = { "/getRptPasaportesIndicadores" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<RptPasaportesIndicadoresDto> getRptPasaportesIndicadores() {
       return Response
                  .<RptPasaportesIndicadoresDto>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.getRptPasaportesIndicadores())
                  .build();
   }

   @GetMapping( path = { "/getRptPasaportesEntregadosPorAños" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptPasaportesPorAñosDto>> getRptPasaportesEntregadosPorAños() {
       return Response
                  .<List<RptPasaportesPorAñosDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.getRptPasaportesEntregadosPorAños())
                  .build();
   }

   @GetMapping( path = { "/getRptPasaportesEntregadosPor12UltimosMeses" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptPasaportesPor12UltimosMesesDto>> getRptPasaportesEntregadosPor12UltimosMeses() {
       return Response
                  .<List<RptPasaportesPor12UltimosMesesDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.getRptPasaportesEntregadosPor12UltimosMeses())
                  .build();
   }

   @GetMapping( path = { "/getRptPasaportesEntregadosPor31UltimosDias" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptPasaportesPor31UltimosDiasDto>> getRptPasaportesEntregadosPor31UltimosDias() {
       return Response
                  .<List<RptPasaportesPor31UltimosDiasDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.getRptPasaportesEntregadosPor31UltimosDias())
                  .build();
   }

}
