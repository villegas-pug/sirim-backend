package com.microservicio.rimreglanegocio.Controllers;

import java.util.List;
import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.utils.Response;
import com.microservicio.rimreglanegocio.services.RNProcesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = { "*" })
@RestController
public class RNProcesoController {
   
   @Autowired
   private RNProcesoService service;

   @GetMapping(path = { "/findAllRNProceso" })
   public Response<List<RNProceso>> findAllRNProceso() {
       return Response
                  .<List<RNProceso>>builder()
                  .data(this.service.findAllRNProceso())
                  .build();
   }

}
