package com.microservicio.rimsim.controllers;

import java.util.List;
import java.util.Map;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.models.entities.Pais;
import com.microservicio.rimsim.services.RimsimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "*" })
@RestController
public class RimsimController {

   @Autowired
   private RimsimService service;

   @GetMapping(path = { "/findTableMetaByNameSim" })
   @ResponseStatus(code = HttpStatus.OK)
   public List<Map<String, String>> findTableMetaByNameSim(@RequestParam String nombreTabla) {
      return this.service.findTableMetaByName(nombreTabla);
   }

   @PostMapping(path = { "/dynamicJoinStatementSim" })
   @ResponseStatus(code = HttpStatus.OK)
   public List<Object[]> dynamicJoinStatementSim(@RequestBody QueryClauseDto queryClauseDto) {

      String mod = queryClauseDto.getMod(),
             fields = queryClauseDto.getFields(),
             where = queryClauseDto.getWhere();

      List<Object[]> result = this.service.dynamicJoinStatement(mod, fields, where);

      return result;
   }

}
