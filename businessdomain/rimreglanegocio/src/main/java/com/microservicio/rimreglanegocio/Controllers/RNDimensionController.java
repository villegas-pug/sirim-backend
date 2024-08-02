package com.microservicio.rimreglanegocio.Controllers;

import java.util.List;
import com.commons.utils.models.entities.RNDimension;
import com.commons.utils.utils.Response;
import com.microservicio.rimreglanegocio.services.RNDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = { "*" })
@RestController
public class RNDimensionController {
   
   @Autowired
   private RNDimensionService service;

   @GetMapping(path = { "/findAllRNDimension" })
   public Response<?> findAllRNDimension() {
       return Response
                  .<List<RNDimension>>builder()
                  .data(this.service.findAllRNDimension())
                  .build();
   }
   

}
