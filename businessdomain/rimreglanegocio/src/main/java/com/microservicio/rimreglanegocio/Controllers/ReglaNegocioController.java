package com.microservicio.rimreglanegocio.Controllers;

import java.util.List;

import com.commons.utils.constants.Messages;
import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.models.entities.ReglaNegocio;
import com.commons.utils.utils.Response;
import com.microservicio.rimreglanegocio.services.ReglaNegocioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = { "*" })
@RestController
public class ReglaNegocioController {
   
   @Autowired
   private ReglaNegocioService service;

   @PostMapping(path = { "/findReglasNegocioByProceso" })
   public Response<List<ReglaNegocio>> findReglasNegocioByProceso(@RequestBody RNProceso proceso) {
       return Response
                  .<List<ReglaNegocio>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.findReglasNegocioByProceso(proceso))
                  .build();
   }
   
   

}
