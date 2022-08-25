package com.commons.utils.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.services.CommonService;
import com.commons.utils.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CommonController<E extends Object, S extends CommonService<E>> {

   @Autowired
   protected S service;

   @GetMapping(path = "/findAll")
   @ResponseStatus(value = HttpStatus.OK)
   protected Response<List<E>> findAll() {
      
      List<E> entityDb = service.findAll();

      if (entityDb.size() == 0) throw new DataAccessEmptyWarning();

      return Response
                  .<List<E>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(entityDb)
                  .build();
   }

   @GetMapping(path = "/properties")
   protected ResponseEntity<?> getProperties(
         @Value("${spring.datasource.url}") String dsUrl,
         @Value("${spring.profiles.active}") String profile,
         @Value("${spring.jpa.hibernate.ddl-auto}") String jpaHibernateDdl,
         @Value("${spring.application.name}") String microservicio, 
         @Value("${server.port}") String port) {
      Map<String, String> properties = new HashMap<>();
      properties.put("datasourceUrl", dsUrl);
      properties.put("profile", profile);
      properties.put("jpaHibernateDdl", jpaHibernateDdl);
      properties.put("microservicio", microservicio);
      properties.put("port", port);

      return ResponseEntity.ok().body(properties);
   }

}