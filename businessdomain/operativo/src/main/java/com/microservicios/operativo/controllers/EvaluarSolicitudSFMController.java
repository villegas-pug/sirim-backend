package com.microservicios.operativo.controllers;

import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Etapa;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.dto.EvaluarSolicitudSFMDto;
import com.microservicios.operativo.models.entities.EvaluarSolicitudSFM;
import com.microservicios.operativo.services.EvaluarSolicitudSFMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "*" })
@RestController
public class EvaluarSolicitudSFMController {

   @Autowired
   private EvaluarSolicitudSFMService service;

   @GetMapping( path = { "/findAllBandejaEvaluacion" } )
   public ResponseEntity<?> findAllBandejaEvaluacion() {

      List<EvaluarSolicitudSFM> evaluarSolicitudSFMDb = this.service.findAll();
      if (evaluarSolicitudSFMDb.size() == 0) throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(evaluarSolicitudSFMDb)
                                       .build());
   }

   @PutMapping( path = { "/readAssignment/{idVerifExp}" } )
   public ResponseEntity<?> readAssignment(@PathVariable Long idVerifExp) {
      
      EvaluarSolicitudSFM evaluarSolicitudSFM = this.service
                                                      .findById(idVerifExp)
                                                      .orElseThrow(DataAccessEmptyWarning::new);

      evaluarSolicitudSFM.setLeido(true);
      this.service.save(evaluarSolicitudSFM);
       
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_TO_READ_ASSIGNMENT_REVISOR)
                                       .data(this.service.findAll())
                                       .build());
   }

   @PostMapping(path = { "/saveDiligencia" })
   public ResponseEntity<?> saveDiligencia(@RequestBody EvaluarSolicitudSFMDto evaluarSolicitudSFMDto) {
       
      EvaluarSolicitudSFM evaluarSolicitudSFM = this.service
                                                         .findById(evaluarSolicitudSFMDto.getIdVerifExp())
                                                         .orElseThrow(DataAccessEmptyWarning::new);

      boolean isNewDiligencia = evaluarSolicitudSFMDto.getDiligencia().getIdDiligencia() == null;

      if (isNewDiligencia) {
         evaluarSolicitudSFM.addDiligenciaSFM(evaluarSolicitudSFMDto.getDiligencia());
      } else {

         long idDiligencia = evaluarSolicitudSFMDto.getDiligencia().getIdDiligencia();
         
         evaluarSolicitudSFM
            .getDiligencia()
            .stream()
            .filter(diligencia -> diligencia.getIdDiligencia().equals(idDiligencia))
            .forEach(diligencia -> {
               diligencia.setNumeroDocumento(evaluarSolicitudSFMDto.getDiligencia().getNumeroDocumento());
               diligencia.setTipoDocumento(evaluarSolicitudSFMDto.getDiligencia().getTipoDocumento());
               diligencia.setFechaDocumento(evaluarSolicitudSFMDto.getDiligencia().getFechaDocumento());
               diligencia.setSolicitudExterna(evaluarSolicitudSFMDto.getDiligencia().isSolicitudExterna());
               diligencia.setFechaSolicitud(evaluarSolicitudSFMDto.getDiligencia().getFechaSolicitud());
               diligencia.setDependenciaDestino(evaluarSolicitudSFMDto.getDiligencia().getDependenciaDestino());
               diligencia.setFechaRespuesta(evaluarSolicitudSFMDto.getDiligencia().getFechaRespuesta());
            });
      }

      this.service.save(evaluarSolicitudSFM);
       
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_DILIGENCIA)
                                       .data(this.service.findAll())
                                       .build());
   }

   @GetMapping( path = { "/findAllEtapa" } )
   public ResponseEntity<?> findAllEtapa() {
      List<Etapa> etapaDb = this.service.findAllEtapa();
      if(etapaDb.size() == 0) throw new DataAccessEmptyWarning();
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(etapaDb)
                                       .build());
   }
 
   @PutMapping(path = { "/updateEtapaSolicitud/{idVerifExp}" })
   public ResponseEntity<?> updateEtapaSolicitud(@PathVariable Long idVerifExp, @RequestBody Etapa etapa) {
      EvaluarSolicitudSFM evaluarSolicitudSFM = this.service
                                                      .findById(idVerifExp)
                                                      .orElseThrow(DataAccessEmptyWarning::new);
      evaluarSolicitudSFM.getBandejaDoc().setEtapa(etapa);
      this.service.save(evaluarSolicitudSFM);

       return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_UPDATE)
                                       .data(this.service.findAll())
                                       .build());
   }


   @PutMapping( path = { "/updateOpinionSolicitud" } )
   public ResponseEntity<?> updateOpinionSolicitud(@RequestBody EvaluarSolicitudSFM evaluarSolicitudSFMNew) {
      EvaluarSolicitudSFM evaluarSolicitudSFM = this.service
                                                      .findById(evaluarSolicitudSFMNew.getIdVerifExp())
                                                      .orElseThrow(DataAccessEmptyWarning::new);
      
      if(evaluarSolicitudSFMNew.getHallazgo().trim().length() > 0) 
         evaluarSolicitudSFM.setHallazgo(evaluarSolicitudSFMNew.getHallazgo());
      else
         evaluarSolicitudSFM.setHallazgo(null);

      if(evaluarSolicitudSFMNew.getRecomendacion().trim().length() > 0)
         evaluarSolicitudSFM.setRecomendacion(evaluarSolicitudSFMNew.getRecomendacion());
      else
         evaluarSolicitudSFM.setRecomendacion(null);

      this.service.save(evaluarSolicitudSFM);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_OPINION)
                                       .data(this.service.findAll())
                                       .build());
   }

}