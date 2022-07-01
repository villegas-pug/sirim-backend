package com.microservicio.rimsim.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Tuple;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.models.dto.QueryClauseDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimsim.models.dto.RequestParamsDnvDto;
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
   public List<Map<String, Object>> findTableMetaByNameSim(@RequestParam String nombreTabla) {
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

   @PostMapping(path = { "/findDnvByParams" })
   public ResponseEntity<?> findDnvByParams(@RequestBody RequestParamsDnvDto params) {
      
      String nacionalidad = Optional.ofNullable(params.getPais().getIdPais()).orElse("%"), 
             dependencia = Optional.ofNullable(params.getDependencia().getIdDependencia()).orElse("%"),
             tipoMov = params.getTipoMov(),
             fecIniMovMig = params.getFecIni().concat("T00:00:00.000"), 
             fecFinMovMig = params.getFecFin().concat("T23:59:59.999");

      List<Tuple> dnvDb = this.service.findDnvByParams(nacionalidad, dependencia, tipoMov, fecIniMovMig, fecFinMovMig);
      if (dnvDb.size() == 0) throw new DataAccessEmptyWarning();
       
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(DataModelHelper.convertTuplesToJson(dnvDb, false))
                                       .build());
   }
   
}
