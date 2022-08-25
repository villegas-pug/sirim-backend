package com.microservicio.rimextraccion.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.RimctrlCalCamposAnalisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "*" })
@RestController
public class RimctrlCalCamposAnalisisController {

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired
   private RimasigGrupoCamposAnalisisService rimasigGrupoService;

   @Autowired
   private RimctrlCalCamposAnalisisService rimctrlCalAnalisisService;

   @PostMapping(path={ "/saveCtrlCalCamposAnalisis" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> saveCtrlCalCamposAnalisis(@RequestParam Long idAsigGrupo) {
      this.rimctrlCalAnalisisService.saveCtrlCalCamposAnalisis(idAsigGrupo);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_CREATE_RECORDS_FOR_CTRLCAL)
               .data(List.of())
               .build();
   }

   @GetMapping(path = { "/findTablaDinamicaByNameAndIds" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findTablaDinamicaByNameAndIds(@RequestParam String nombreTabla, @RequestParam String idsCsv) {

      // ► Data-Set ...
      List<Map<String, Object>> tablaDinamicaDb = this.rimctrlCalAnalisisService.findTablaDinamicaByNameAndIds(nombreTabla, idsCsv);

      return Response
                  .<List<Map<String, Object>>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(tablaDinamicaDb)
                  .build();
   }

   @PutMapping( path = { "/setValidationResultOfCtrlCal" } )
   public Response<List<TablaDinamicaDto>> setValidationResultOfCtrlCal(@RequestBody AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto) {
      
      // ► Save ...
      this.rimctrlCalAnalisisService.conformToCtrlCal(asigGrupoCamposAnalisisDto);

      // ► Data-Set ...
      List<TablaDinamicaDto> ds = this.rimcommonService.findAllTablaDinamica();
       
      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .message(Messages.SUCCESS_RESULT_CONFORMITY_CTRLCAL)
                  .data(ds)
                  .build();
   }
   
   @PutMapping( path = { "/validateRecordAssigned" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> validateRecordAssigned(@RequestBody RecordAssignedDto recordAssignedDto) {

      // ► Dep's ...
      String nombreTabla = recordAssignedDto.getNombreTable();
      Long idAsig = recordAssignedDto.getAsigGrupo().getIdAsigGrupo();
      Long idCtrlCal = recordAssignedDto.getIdCtrlCal();

      // ► Repo dep's ...      
      String idsCsv = Optional.ofNullable(this.rimasigGrupoService
                                                     .findById(idAsig))
                                                     .orElseThrow(DataAccessEmptyWarning::new)
                                                     .getCtrlsCalCamposAnalisis()
                                                     .stream()
                                                     .filter(ctrlCal -> ctrlCal.getIdCtrlCal().equals(idCtrlCal))
                                                     .map(ctrlCal -> ctrlCal.getIdsCtrlCalCsv())
                                                     .collect(Collectors.joining());

      // ► Save ...
      this.rimctrlCalAnalisisService.validateRecordAssigned(recordAssignedDto);

      // ► Data - Set ...
      List<Map<String, Object>> ds = this.rimctrlCalAnalisisService.findTablaDinamicaByNameAndIds(nombreTabla, idsCsv);

      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.SUCCESS_VALIDATE_RECORD_CTRLCAL)
               .data(ds)
               .build();
   }

}