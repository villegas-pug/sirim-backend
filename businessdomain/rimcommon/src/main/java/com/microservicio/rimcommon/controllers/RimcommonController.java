package com.microservicio.rimcommon.controllers;

import java.util.List;
import java.util.Map;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimcommon.services.RimcommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" })
@RestController
public class RimcommonController {

   @Autowired
   private RimcommonService rimcommonService;

   @GetMapping( path = { "/findAllTablaDinamica" }, produces = { MediaType.APPLICATION_JSON_VALUE })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> findAllTablaDinamica() {
      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimcommonService.findAllTablaDinamica())
                  .build();
   }

   @GetMapping( path = { "/findAllTablaDinamicaOnlyNombres" }, produces = { MediaType.APPLICATION_JSON_VALUE })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> findAllTablaDinamicaOnlyNombres() {
      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimcommonService.findAllTablaDinamicaOnlyNombres())
                  .build();
   }

   @PostMapping( path = { "/findTablaDinamicaByUsrCreador" }, produces = { MediaType.APPLICATION_JSON_VALUE })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> findTablaDinamicaByUsrCreador(@RequestBody Usuario usrCreador) {

      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonService.findTablaDinamicaByUsrCreador(usrCreador);
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
      List<Map<String, Object>> tablaDinamicaDb = this.rimcommonService.findTablaDinamicaBySuffixOfField(nombreTabla, suffix);
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return Response
               .<List<Map<String, Object>>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(tablaDinamicaDb)
               .build();
   }

   @GetMapping(path = { "/findTablaDinamicaByRangoFromIds" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findTablaDinamicaByRangoFromIds(
                                                         @RequestParam(name = "idAsigGrupo") long idAsigGrupo, 
                                                         @RequestParam(name = "regAnalisisIni") long rIni, 
                                                         @RequestParam(name = "regAnalisisFin") long rFin) {
      
      return Response
                  .<List<Map<String, Object>>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimcommonService.findTablaDinamicaByRangoFromIds(idAsigGrupo, rIni, rFin))
                  .build();
   }
   
   @GetMapping( path = { "/findGrupoCamposAnalisisById" }, produces = { MediaType.APPLICATION_JSON_VALUE } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<GrupoCamposAnalisisDto> findGrupoCamposAnalisisById(@RequestParam Long idGrupo) {
      return Response
                  .<GrupoCamposAnalisisDto>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimcommonService.findGrupoCamposAnalisisById(idGrupo))
                  .build();
   }

   @DeleteMapping(path = { "/deleteGrupoCamposAnalisisbyId/{idGrupo}" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> deleteGrupoCamposAnalisisbyId(@PathVariable Long idGrupo){
      this.rimcommonService.deleteGrupoCamposAnalisisbyId(idGrupo);
      return Response
                  .<List<?>>builder()
                  .message(Messages.SUCCESS_DELETE_GRUPO_ANALISIS)
                  .data(List.of())
                  .build();
   }

   @GetMapping( path = { "/countTablaByNombre" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<Long> countTablaByNombre(@RequestParam String nombreTabla) {
      return Response
               .<Long>builder()
               .message(HttpStatus.OK.name())
               .data(this.rimcommonService.countTablaByNombre(nombreTabla))
               .build();
   }

   @GetMapping( path = { "/findDynamicSelectStatement" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> findDynamicSelectStatement(@RequestParam String queryString) {
      return Response
                  .<List<Map<String, Object>>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimcommonService.findDynamicSelectStatement(queryString))
                  .build();
   }
   
   @PutMapping( path = { "/saveRecordAssigned" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<Map<String, Object>>> saveRecordAssigned(@RequestBody RegistroTablaDinamicaDto registroTablaDinamicaDto){

      // ► Save ...
      this.rimcommonService.saveRecordAssigned(registroTablaDinamicaDto);

      // ► Record-Set ...
      List<Map<String, Object>> tdByRangoFromIds = this.rimcommonService
                                                               .findTablaDinamicaByRangoFromIds(
                                                                              registroTablaDinamicaDto.getAsigGrupo().getIdAsigGrupo(),
                                                                              registroTablaDinamicaDto.getRegAnalisisIni(),
                                                                              registroTablaDinamicaDto.getRegAnalisisFin());

      return Response
                  .<List<Map<String, Object>>>builder()
                  .message(Messages.SUCCESS_SAVE_ANALISIS_EXTRACCION)
                  .data(tdByRangoFromIds)
                  .build();
   }

   @GetMapping( path = { "/findTablaDinamicaByNombre" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<TablaDinamicaDto> findTablaDinamicaByNombre(@RequestParam String nombreTabla) {
      return Response
               .<TablaDinamicaDto>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(this.rimcommonService.findTablaDinamicaByNombre(nombreTabla))
               .build();
   }
   
   @PostMapping( path = { "/findAsigAnalisisByUsr" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<AsigGrupoCamposAnalisisDto>> findAsigAnalisisByUsr(@RequestBody AsigRequestBody asigRequestBody) {
      List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisisDb = this.rimcommonService.findAsigByUsrAnalista(asigRequestBody.usrAnalista, asigRequestBody.unfinished);
      return Response
                  .<List<AsigGrupoCamposAnalisisDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(asigGrupoCamposAnalisisDb)
                  .build();
   }

   @GetMapping( path = { "/findAsigById" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<AsigGrupoCamposAnalisisDto> findAsigById(@RequestParam Long idAsig) {
      AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto = this.rimcommonService.findAsigById(idAsig);
      return Response
                  .<AsigGrupoCamposAnalisisDto>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(asigGrupoCamposAnalisisDto)
                  .build();
   }

   //#region Api Client Rest ...
   //#endregion
   
   public static class AsigRequestBody {
      public Usuario usrAnalista;
      public boolean unfinished;
   }
}