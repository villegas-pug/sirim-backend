package com.microservicio.rimctrlcalidad.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimctrlcalidad.services.RimctrlCalidadService;
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
public class RimctrlCalidadController {

   @Autowired
   private RimctrlCalidadService rimctrlCalidadService;

   @PostMapping(path={ "/saveCtrlCalCamposAnalisis" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> saveCtrlCalCamposAnalisis(@RequestParam Long idAsigGrupo) {
      this.rimctrlCalidadService.saveCtrlCalCamposAnalisis(idAsigGrupo);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_CREATE_RECORDS_FOR_CTRLCAL)
               .data(List.of())
               .build();
   }

   @GetMapping(path = { "/findTablaDinamicaByIdCtrlCalAndIds" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findTablaDinamicaByIdCtrlCalAndIds(@RequestParam Long idCtrlCal, @RequestParam String idsCsv) {

      // ► Data-Set ...
      List<Map<String, Object>> tablaDinamicaDb = this.rimctrlCalidadService.findTablaDinamicaByIdCtrlCalAndIds(idCtrlCal, idsCsv);

      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(tablaDinamicaDb)
               .build();
   }

   @PutMapping( path = { "/validateRecordAssigned" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> validateRecordAssigned(@RequestBody RegistroTablaDinamicaDto recordAssignedDto) {

      // ► Dep's ...
      Long idAsig = recordAssignedDto.getAsigGrupo().getIdAsigGrupo();
      Long idCtrlCal = recordAssignedDto.getIdCtrlCal();

      // ► Repo dep's ...
      String idsCsv = this.rimctrlCalidadService.findAsigGrupoCamposAnalisisById(idAsig)
                                                .getCtrlsCalCamposAnalisis()
                                                .stream()
                                                .filter(ctrlCal -> ctrlCal.getIdCtrlCal().equals(idCtrlCal))
                                                .map(ctrlCal -> ctrlCal.getIdsCtrlCalCsv())
                                                .collect(Collectors.joining());

      // ► Save ...
      this.rimctrlCalidadService.validateRecordAssigned(recordAssignedDto);

      // ► Data - Set ...
      List<Map<String, Object>> ds = this.rimctrlCalidadService.findTablaDinamicaByIdCtrlCalAndIds(idCtrlCal, idsCsv);

      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.SUCCESS_VALIDATE_RECORD_CTRLCAL)
               .data(ds)
               .build();
   }

   @PutMapping( path = { "/saveMetaFieldIdErrorCsv" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<?> saveMetaFieldIdErrorCsv(@RequestBody RegistroTablaDinamicaDto registroTablaDinamica) {
      this.rimctrlCalidadService.saveMetaFieldIdErrorCsv(registroTablaDinamica);
      return Response
                  .builder()
                  .message(Messages.SUCCESS_SAVE_DATA_MODEL)
                  .data(List.of())
                  .build();
   }

   @PutMapping( path = { "/setValidationResultOfCtrlCal" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> setValidationResultOfCtrlCal(@RequestBody AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto) {
      
      // ► Save ...
      this.rimctrlCalidadService.conformToCtrlCal(asigGrupoCamposAnalisisDto);
       
      return Response
                  .<List<?>>builder()
                  .message(Messages.SUCCESS_RESULT_CONFORMITY_CTRLCAL)
                  .data(List.of())
                  .build();
   }

   @GetMapping(path = { "/findAsigGrupoCamposAnalisisById" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<AsigGrupoCamposAnalisisDto> findAsigGrupoCamposAnalisisById(@RequestParam Long idAsigGrupo) {
      return Response
                  .<AsigGrupoCamposAnalisisDto>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimctrlCalidadService.findAsigGrupoCamposAnalisisById(idAsigGrupo))
                  .build();
   }

}