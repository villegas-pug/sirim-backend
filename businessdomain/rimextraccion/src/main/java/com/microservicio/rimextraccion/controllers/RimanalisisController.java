package com.microservicio.rimextraccion.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.constants.RimHttpHeaders;
import com.microservicio.rimextraccion.dto.AnalizadosDto;
import com.microservicio.rimextraccion.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import com.microservicio.rimextraccion.services.RimasigGrupoCamposAnalisisService;
import com.microservicio.rimextraccion.services.RimanalisisService;
import com.microservicio.rimextraccion.services.RimcommonService;
import com.microservicio.rimextraccion.services.RimextraccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "*" })
@RestController
public class RimanalisisController {

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired
   private RimextraccionService rimextraccionService;

   @Autowired
   private RimasigGrupoCamposAnalisisService asigAnalisisService;

   @Autowired
   private RimanalisisService rimanalisisService;

   @Value("classpath:static/S10.DRCM.FR.001-Registro de depuracion de datos_V02.xlsx")
   private Resource prodAnalisisXlsx;

   @PostMapping( path = { "/findAsigAnalisisByUsr" } )
   public ResponseEntity<?> findAsigAnalisisByUsr(@RequestBody Usuario usrAnalista) {
      List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisisDb = this.asigAnalisisService.findByUsrAnalista(usrAnalista);
      if (asigGrupoCamposAnalisisDb.size() == 0) 
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                 .data(asigGrupoCamposAnalisisDb)
                                 .build());
   }
   
   @GetMapping( path = { "/findTablaDinamicaByRangoFromIds" } )
   public ResponseEntity<?> findTablaDinamicaByRangoFromIds(
                                             @RequestParam String nombreTabla, 
                                             @RequestParam Long regAnalisisIni,
                                             @RequestParam Long regAnalisisFin){
      List<Map<String, Object>> tablaDinamicaDb = this.rimcommonService.findTablaDinamicaByRangoFromIds(nombreTabla, regAnalisisIni, regAnalisisFin);
      if(tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(tablaDinamicaDb)
                                       .build());
   }

   @PutMapping( path = { "/saveRecordAssigned" } )
   public ResponseEntity<?> saveRecordAssigned(@RequestBody RecordAssignedDto recordAssignedDto){

      /*► Payload ...  */
      String cleanJsonValues = recordAssignedDto.getValues().replaceAll("[{}]", ""),
             csvFields = Arrays.stream(cleanJsonValues.split(","))
                                 .map(i -> { 
                                    String[] field = i.split(":");
                                    return 
                                       field[0].replaceAll("[\"\']", "").concat("=").concat(field[1].replaceAll("\"", "'"));
                                 })
                                 .collect(Collectors.joining(", "));
         
      /*► Query-String ... */
      StringBuilder queryString = new StringBuilder("UPDATE ");
      queryString
         .append(recordAssignedDto.getNombreTable()).append(" SET ").append(csvFields)
         .append(" WHERE nId = ").append(recordAssignedDto.getId());

      /*► Save: Registro en tabla dinámica ... */
      this.rimextraccionService.alterTablaDinamica(queryString.toString());

      /*► Save: ... */
      /*► Search: Grupo asignado ...  */
      /*-------------------------------------------------------------------------------------------------------*/
      Long idAsigGrupo = recordAssignedDto.getAsigGrupo().getIdAsigGrupo();
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.asigAnalisisService.findById(idAsigGrupo);

      /*► Si producción está registrada ... */
      Long idRegistro = recordAssignedDto.getId();
      boolean isRegistered = asigGrupoCamposAnalisis
                                 .getProduccionAnalisis()
                                 .stream()
                                 .anyMatch(prod -> prod.getAsigGrupo().getIdAsigGrupo().equals(idAsigGrupo) 
                                                            && prod.getIdRegistroAnalisis().equals(idRegistro));

      if (isRegistered) {
         asigGrupoCamposAnalisis.getProduccionAnalisis()
                                    .stream()
                                    .filter(prod -> prod.getAsigGrupo().getIdAsigGrupo().equals(idAsigGrupo) 
                                                         && prod.getIdRegistroAnalisis().equals(idRegistro))
                                    .forEach(prod -> prod.setFechaFin(new Date()));
      } else {
         asigGrupoCamposAnalisis.addProduccionAnalisis(ProduccionAnalisis.of().idRegistroAnalisis(recordAssignedDto.getId()).get());
      }

      /*► Save ... */
      this.asigAnalisisService.save(asigGrupoCamposAnalisis);

      /*-------------------------------------------------------------------------------------------------------*/

      /*► Record-Set ... */
      List<Map<String, Object>> tdByRangoFromIds = this.rimcommonService.findTablaDinamicaByRangoFromIds(
                                                                     recordAssignedDto.getNombreTable(),
                                                                     recordAssignedDto.getRegAnalisisIni(),
                                                                     recordAssignedDto.getRegAnalisisFin());

      return ResponseEntity.ok(
                              Response
                                 .builder()
                                 .message(Messages.SUCCESS_SAVE_ANALISIS_EXTRACCION)
                                 .data(tdByRangoFromIds)
                                 .build());
   }

   @PostMapping( path = { "/downloadAnalisadosByDates" } )
   public ResponseEntity<?> downloadAnalisadosByDates(@RequestBody AnalizadosDto analizadosDto) throws IOException {
         
      /* ► Dep's ... */
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      ByteArrayResource byteArrResource = null;
      String fileName = prodAnalisisXlsx.getFilename(),
             usrAnalista = this.asigAnalisisService.findById(analizadosDto.getIdAsigGrupo()).getUsrAnalista().getNombres();

      /* ► Header's ... */
      HttpHeaders headers = new HttpHeaders();
      String contentDisposition = String.format("attachment; filename=\"%s - %s(%s).xlsx\"", fileName, usrAnalista, df.format(new Date()));
      headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);

      byteArrResource = this.rimanalisisService.convertProduccionAnalisisToByteArrResource(analizadosDto);

      return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .headers(headers)
            .body(byteArrResource);

   }
   
   
}