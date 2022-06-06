package com.microservicio.nacionalizacion.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.commons.utils.constants.Etapas;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.controllers.CommonController;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Etapa;
import com.commons.utils.models.entities.TipoTramite;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.nacionalizacion.models.dto.EvalRequisitoTramiteNacDto;
import com.microservicio.nacionalizacion.models.dto.EvaluarTramiteNacDto;
import com.microservicio.nacionalizacion.models.dto.NacionalizacionRptDto;
import com.microservicio.nacionalizacion.models.entities.EtapaTramiteNac;
import com.microservicio.nacionalizacion.models.entities.EvalRequisitoTramiteNac;
import com.microservicio.nacionalizacion.models.entities.EvaluarTramiteNac;
import com.microservicio.nacionalizacion.models.entities.Nacionalizacion;
import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;
import com.microservicio.nacionalizacion.models.entities.RequisitoTipoTramite;
import com.microservicio.nacionalizacion.models.enums.EstadoEtapa;
import com.microservicio.nacionalizacion.models.enums.EstadoTramite;
import com.microservicio.nacionalizacion.services.EtapaTramiteNacService;
import com.microservicio.nacionalizacion.services.EvalRequisitoTramiteNacService;
import com.microservicio.nacionalizacion.services.EvaluarTramiteNacService;
import com.microservicio.nacionalizacion.services.NacionalizacionService;
import com.microservicio.nacionalizacion.services.NuevoTramiteNacService;
import com.microservicio.nacionalizacion.services.RequisitoTipoTramiteService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@CrossOrigin(origins = { "*" }, exposedHeaders = { "*" })
@RestController
public class NacionalizacionCotroller extends CommonController<Nacionalizacion, NacionalizacionService> {

   @Value("classpath:static/rpt_nacionalizacion.xlsx")
   private Resource rptNacionalizacion;

   @Autowired
   private NuevoTramiteNacService nuevoTramiteNacService;

   @Autowired
   private EvaluarTramiteNacService evaluarTramiteNacService;

   @Autowired
   private EtapaTramiteNacService etapaTramiteNacService;

   @Autowired
   private RequisitoTipoTramiteService requisitoTipoTramiteService;

   @Autowired
   private EvalRequisitoTramiteNacService evalRequisitoTramiteNacService;

   @GetMapping(path = "/countProcPendiente")
   public ResponseEntity<?> countProcPendiente() {
      List<NacionalizacionRptDto> nacionalizacionDb = super.service.countProcPendiente();
      if (nacionalizacionDb.size() == 0) 
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
         Response
            .builder()
            .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
            .data(nacionalizacionDb)
            .build()
      );
   }

   @GetMapping(path = "/downloadProcByCustomFilter")
   public ResponseEntity<?> downloadProcByCustomFilter(
      @RequestParam String año,
      @RequestParam(required = false, defaultValue = "") String tipo,
      @RequestParam(required = false, defaultValue = "") String etapa
   ) throws IOException {
      
      List<Nacionalizacion> nacionalizacionDb = super.service.findProcByCustomFilter(año, tipo, etapa);

      if(nacionalizacionDb.size() == 0)
         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

      ByteArrayOutputStream output = new ByteArrayOutputStream();

      try (XSSFWorkbook book = new XSSFWorkbook(rptNacionalizacion.getInputStream())) {
         XSSFSheet sheet = book.getSheet("»rpt");
         int iRow = 1;
         for (Nacionalizacion nac : nacionalizacionDb){
            XSSFRow row = sheet.getRow(iRow + 2);
            row.getCell(0).setCellValue(iRow);
            row.getCell(1).setCellValue(nac.getNumeroTramite());
            row.getCell(2).setCellValue(nac.getFechaTramite());
            row.getCell(3).setCellValue(nac.getAñoTramite());
            row.getCell(4).setCellValue(nac.getTipoTramite());
            row.getCell(5).setCellValue(nac.getEstadoTramite());
            row.getCell(6).setCellValue(nac.getEtapaTramite());
            row.getCell(7).setCellValue(nac.getFechaAud());
            row.getCell(8).setCellValue(nac.getDependencia());
            row.getCell(9).setCellValue(nac.getAdministrado());
            row.getCell(10).setCellValue(nac.getSexo());
            row.getCell(11).setCellValue(nac.getPaisNacimiento());
            iRow++;
         }
         book.write(output);
      } catch (Exception e) {
         return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .body(null);
      }

      String contentDisposition = "attachment; filename=\"%s_%s.xlsx\"";
      HttpHeaders header = new HttpHeaders();
      header.add(
         HttpHeaders.CONTENT_DISPOSITION,
         String.format(contentDisposition, rptNacionalizacion.getFilename().split(".xlsx")[0], año));

      
      if (nacionalizacionDb.size() == 0) 
         throw new DataAccessEmptyWarning();


      return ResponseEntity
               .ok()
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .headers(header)
               .body(new ByteArrayResource(output.toByteArray()));
   }
 
   @PostMapping( path = "/uploadTramiteNac" )
   public ResponseEntity<?> uploadTramiteNac(@RequestPart Usuario operadorDesig, @RequestPart MultipartFile file) {

      LocalDateTime corteFechaTramiteLDT = LocalDateTime.of(2021, Month.NOVEMBER, 1, 0, 0, 0);
      Date corteFechaTramite = Date.from(corteFechaTramiteLDT.atZone(ZoneId.systemDefault()).toInstant());
      List<NuevoTramiteNac> lstNuevoTramiteNac = new ArrayList<>();
      List<NuevoTramiteNac> nuevoTramiteNacPrevDb = this.nuevoTramiteNacService.findAll();

      try (XSSFWorkbook book = new XSSFWorkbook(file.getInputStream())) {
         XSSFSheet sheet = book.getSheetAt(0);

         for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
            
            XSSFRow row = sheet.getRow(i);

            /*» Valida: Si, fila es nula, termina iteracción ...  */
            if (row.getCell(2) == null) break;

            String numeroTramite = row.getCell(5).getStringCellValue();
            Date fechaTramite = row.getCell(7).getDateCellValue();

            /*» Valida: Si, ...  */
            boolean isOld = nuevoTramiteNacPrevDb
                              .stream()
                              .anyMatch(record -> record.getNumeroTramite().equals(numeroTramite));
            if (isOld) continue;

            /*» Valida: Si, ...  */
            if(fechaTramite.before(corteFechaTramite)) continue;

            EtapaTramiteNac etapaTramiteNac =  EtapaTramiteNac
                                                   .of()
                                                   .etapa(Etapa.of().idEtapa(Etapas.RECEPCION).get())
                                                   .fechaHoraFin(new Date())
                                                   .usrInicia(operadorDesig)
                                                   .usrFinaliza(operadorDesig)
                                                   .estado(EstadoEtapa.F)
                                                   .get();

            NuevoTramiteNac nuevoTramiteNac = new NuevoTramiteNac();
            nuevoTramiteNac.setNumeroTramite(numeroTramite);
            nuevoTramiteNac.setIdTipoTramite((long)row.getCell(3).getNumericCellValue());
            nuevoTramiteNac.setTipoTramite(row.getCell(8).getStringCellValue());
            nuevoTramiteNac.setFechaTramite(fechaTramite);
            nuevoTramiteNac.setAdministrado(row.getCell(14).getStringCellValue());
            nuevoTramiteNac.setSexo(row.getCell(15).getStringCellValue());
            nuevoTramiteNac.setEstadoTramite(EstadoTramite.P);
            nuevoTramiteNac.setEtapa(Etapa.of().idEtapa(Etapas.RECEPCION).get());
            nuevoTramiteNac.setPaisNacimiento(row.getCell(17).getStringCellValue());
            nuevoTramiteNac.setDependencia(row.getCell(13).getStringCellValue());
            nuevoTramiteNac.setFechaAud(row.getCell(12).getDateCellValue());
            nuevoTramiteNac.addEtapaTramiteNac(etapaTramiteNac);
            lstNuevoTramiteNac.add(nuevoTramiteNac);
         }

         /*» Save-Changes ...  */
         this.nuevoTramiteNacService.saveAll(lstNuevoTramiteNac);

      } catch (Exception e) {
         log.error(e.getMessage());
         return ResponseEntity.ok().body(
               Response
                  .builder()
                  .levelLog(LevelLog.WARNING)
                  .message(Messages.MESSAGE_WARNING_FILE_SAVE)
                  .data(Arrays.asList())
                  .build());
      }

      return ResponseEntity.ok().body(
               Response
                  .builder()
                  .message(Messages.SUCCESS_UPLOAD_FILE)
                  .data(this.getAllNuevoTramiteNac())
                  .build());
   }
   
   @GetMapping(path = "/findAllNuevoTramiteNac")
   public ResponseEntity<?> findAllNuevoTramiteNac() {
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.getAllNuevoTramiteNac())
                                       .build());
   }
 
   @PutMapping(path = "/saveFechaMPInNuevoTramiteNac")
   public ResponseEntity<?> saveFechaMPInNuevoTramiteNac(@RequestBody NuevoTramiteNac nuevoTramiteNac) {

      NuevoTramiteNac nuevoTramiteNacUpdated = this.nuevoTramiteNacService
                                                      .findByNumeroTramite(nuevoTramiteNac.getNumeroTramite())
                                                      .orElseThrow(DataAccessEmptyWarning::new);
      nuevoTramiteNacUpdated.setFechaRecepcionMP(nuevoTramiteNac.getFechaRecepcionMP());
      this.nuevoTramiteNacService.save(nuevoTramiteNacUpdated);
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_UPDATE)
                                       .data(this.getAllNuevoTramiteNac())
                                       .build());
   }

   @PutMapping(path = "/toAssignRevisor")
   public ResponseEntity<?> toAssignRevisor(@RequestBody NuevoTramiteNac nuevoTramiteNac) {
      
      Usuario usrRevisor = nuevoTramiteNac.getEvaluarTramiteNac().getOperadorDesig();
      
      /*» Actualiza `etapa actual` del trámite  ... */
      NuevoTramiteNac nuevoTramiteNacUpdated = this.nuevoTramiteNacService
                                                      .findByNumeroTramite(nuevoTramiteNac.getNumeroTramite())
                                                      .orElseThrow(DataAccessEmptyWarning::new);

      nuevoTramiteNacUpdated.setEtapa(Etapa.of().idEtapa(Etapas.EVALUACION).get());
      
      /*» Inserta en `EvaluarTramiteNac` ... */
      nuevoTramiteNacUpdated.setEvaluarTramiteNac(EvaluarTramiteNac
                                                      .of()
                                                      .operadorDesig(usrRevisor)
                                                      .get());

      /*» Save-Changes ... */
      this.nuevoTramiteNacService.save(nuevoTramiteNacUpdated);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_TO_ASSIGN_REVISOR(usrRevisor.getNombres()))
                                       .data(this.getAllNuevoTramiteNac())
                                       .build());
   }
   
   @DeleteMapping(path = "/unassignRevisor")
   public ResponseEntity<?> unassignRevisor(@RequestBody NuevoTramiteNac nuevoTramiteNac) {

      /*» Update: Actualiza `etapa actual` del trámite  ... */
      NuevoTramiteNac nuevoTramiteNacUpdated = this.nuevoTramiteNacService
                                                      .findByNumeroTramite(nuevoTramiteNac.getNumeroTramite())
                                                      .orElseThrow(DataAccessEmptyWarning::new);

      nuevoTramiteNacUpdated.setEtapa(Etapa.of().idEtapa(Etapas.RECEPCION).get());
      nuevoTramiteNacUpdated.removeEvaluarTramiteNac(nuevoTramiteNac.getEvaluarTramiteNac());
      this.nuevoTramiteNacService.save(nuevoTramiteNacUpdated);
      
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_TO_UNASSIGN_REVISOR)
                                       .data(this.getAllNuevoTramiteNac())
                                       .build());
   }

   @PostMapping(path = "/findAllAssignedInEvalByUsrDesig")
   public ResponseEntity<?> findAllAssignedInEvalByUsrDesig(@RequestBody Usuario usrDesign) {
       return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.getAllAssignedInEvalByUsrDesig(usrDesign))
                                       .build());
   }
   
   @PutMapping(path = "/readAssignment")
   public ResponseEntity<?> readAssignment(@RequestBody EvaluarTramiteNac evaluarTramiteNac) {

      /*» Registra nueva etapa en `SidEtapaTramiteNac`  */
      EtapaTramiteNac etapaTramiteNac = EtapaTramiteNac
                                             .of()
                                             .usrInicia(evaluarTramiteNac.getOperadorDesig())
                                             .etapa(Etapa.of().idEtapa(Etapas.EVALUACION).get())
                                             .estado(EstadoEtapa.I)
                                             .tramiteNac(evaluarTramiteNac.getTramiteNac())
                                             .get();
      this.etapaTramiteNacService.save(etapaTramiteNac);
      
      /*» Actualiza `SidEvaluarTramiteNac` a `leido` ... */
      EvaluarTramiteNac evaluarTramiteNacUpdated = this.evaluarTramiteNacService
                                                            .findById(evaluarTramiteNac.getIdVerifExp())
                                                            .orElseThrow(DataAccessEmptyWarning::new);
      evaluarTramiteNacUpdated.setLeido(true);
      this.evaluarTramiteNacService.save(evaluarTramiteNacUpdated);

      /*» Lista requisitos por tipo de trámite ...  */
      List<RequisitoTipoTramite> requisitoTipoTramiteDb = this.requisitoTipoTramiteService
                                                                  .findByTipoTramite(TipoTramite
                                                                                       .of()
                                                                                       .idTipoTramite(evaluarTramiteNac
                                                                                                         .getTramiteNac()
                                                                                                         .getIdTipoTramite())
                                                                                       .get());
      if(requisitoTipoTramiteDb.size() == 0) throw new DataAccessEmptyWarning();

      /*» Registra en `SidEvalRequisitoTramiteNac`  */
      List<EvalRequisitoTramiteNac> evalRequisitoTramiteNacNew = new ArrayList<>();
      requisitoTipoTramiteDb.forEach(requisitoTipoTramite -> {
                                          evalRequisitoTramiteNacNew.add(
                                                EvalRequisitoTramiteNac
                                                      .of()
                                                      .evaluarTramiteNac(evaluarTramiteNacUpdated)
                                                      .requisitoTipoTramite(requisitoTipoTramite)
                                                      .get());
      });
      this.evalRequisitoTramiteNacService.saveAll(evalRequisitoTramiteNacNew);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_TO_READ_ASSIGNMENT_REVISOR)
                                       .data(this.getAllAssignedInEvalByUsrDesig(evaluarTramiteNac.getOperadorDesig()))
                                       .build());
   }

   @PutMapping(path = "/checkRequisitoTramiteNac")
   public ResponseEntity<?> checkRequisitoTramiteNac(@RequestBody EvalRequisitoTramiteNacDto evalRequisitoTramiteNac) {
      /*» Actualiza  `evalRequisitoTramiteNacUpdated` */
      EvalRequisitoTramiteNac evalRequisitoTramiteNacUpdated = this.evalRequisitoTramiteNacService
                                                                        .findById(evalRequisitoTramiteNac.getIdEvalReqTramite())
                                                                        .orElseThrow(DataAccessEmptyWarning::new);

                                                                       
      evalRequisitoTramiteNacUpdated.setEstado(evalRequisitoTramiteNac.getEstado());
      evalRequisitoTramiteNacUpdated.setFechaHoraFin(evalRequisitoTramiteNac.getEstado() == EstadoEtapa.F ? new Date() : null);

      this.evalRequisitoTramiteNacService.save(evalRequisitoTramiteNacUpdated);

      /*»  */
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.getAllAssignedInEvalByUsrDesig(evalRequisitoTramiteNac.getUsrDesig()))
                                       .build());
   }

   @PutMapping(path = "/finishEvaluarTramiteNac")
   public ResponseEntity<?> finishEvaluarTramiteNac(@RequestBody EvaluarTramiteNac evalTramiteNac) {

      /*» Actualiza `SidEvaluarTramiteNac` ... */
      EvaluarTramiteNac evaluarTramiteNacUpdated = this.evaluarTramiteNacService
                                                            .findById(evalTramiteNac.getIdVerifExp())
                                                            .orElseThrow(DataAccessEmptyWarning::new);
      evaluarTramiteNacUpdated.setCompletado(true);
      evaluarTramiteNacUpdated.setFechaHoraFin(new Date());
      this.evaluarTramiteNacService.save(evaluarTramiteNacUpdated);

      /*» Actualiza `SidEtapaTramiteNac` ... */
      Long idTramiteNac = evalTramiteNac.getTramiteNac().getIdTramiteNac(),
           idEtapa      = evalTramiteNac.getTramiteNac().getEtapa().getIdEtapa();        
      EtapaTramiteNac etapaTramiteNacUpdated = this.etapaTramiteNacService
                                                      .findByIdTramiteNacAndIdEtapa(idTramiteNac, idEtapa)
                                                      .orElseThrow(DataAccessEmptyWarning::new);

      etapaTramiteNacUpdated.setEstado(EstadoEtapa.F);
      etapaTramiteNacUpdated.setFechaHoraFin(new Date());
      etapaTramiteNacUpdated.setUsrFinaliza(evalTramiteNac.getOperadorDesig());
      this.etapaTramiteNacService.save(etapaTramiteNacUpdated);

      /*» Response:  */
      List<EvaluarTramiteNacDto> evaluarTramiteNacDto = this.getAllAssignedInEvalByUsrDesig(evalTramiteNac.getOperadorDesig());
      if(evaluarTramiteNacDto.size() == 0) throw new DataAccessEmptyWarning();

      /*»  */
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(evaluarTramiteNacDto)
                                       .build());
   }

   //#region: Métodos privados

   private List<NuevoTramiteNac> getAllNuevoTramiteNac(){
      List<NuevoTramiteNac> nuevoTramiteNacDb = this.nuevoTramiteNacService.findAll();
      if(nuevoTramiteNacDb.size() == 0) throw new DataAccessEmptyWarning();
      return nuevoTramiteNacDb
                  .stream()
                  .sorted((prev, next) -> prev.getFechaTramite().compareTo(next.getFechaTramite()))
                  .collect(Collectors.toList());
   }

   private List<EvaluarTramiteNacDto> getAllAssignedInEvalByUsrDesig(Usuario operadorDesig){
      
      List<EvaluarTramiteNac> evaluarTramiteNacDb = this.evaluarTramiteNacService.findAll();
      if(evaluarTramiteNacDb.size() == 0) throw new DataAccessEmptyWarning();

      Predicate<EvaluarTramiteNac> byOperadorDesig = evalTramiteNac -> evalTramiteNac.getOperadorDesig().equals(operadorDesig);
      Predicate<EvaluarTramiteNac> isCompletado = evalTramiteNac -> evalTramiteNac.isCompletado();

      List<EvaluarTramiteNac> evaluarTramiteNacFiltered = evaluarTramiteNacDb
                                                               .stream()
                                                               .filter(byOperadorDesig.and(isCompletado.negate()))
                                                               .sorted((prev, next) -> prev.getFechaDerivacion().compareTo(next.getFechaDerivacion()))
                                                               .collect(Collectors.toList());

      return evaluarTramiteNacFiltered
                  .stream()
                  .map(record -> new ModelMapper().map(record, EvaluarTramiteNacDto.class))
                  .collect(Collectors.toList());
   }

   //#endregion
}