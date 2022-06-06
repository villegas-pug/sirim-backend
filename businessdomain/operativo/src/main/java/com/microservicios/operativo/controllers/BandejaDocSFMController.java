package com.microservicios.operativo.controllers;


import java.util.List;

import com.commons.utils.constants.Etapas;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Etapa;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.models.entities.BandejaDocSFM;
import com.microservicios.operativo.models.entities.EvaluarSolicitudSFM;
import com.microservicios.operativo.services.BandejaDocSFMService;
import com.microservicios.operativo.services.TipoTramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin( origins = { "*" } )
@RestController
public class BandejaDocSFMController {
   
   @Autowired
   private BandejaDocSFMService service;

   @Autowired
   private TipoTramiteService tipoTramiteService;

   @PostMapping( path = { "/saveSolicitud" }, consumes = { MediaType.APPLICATION_JSON_VALUE } )
   public ResponseEntity<?> saveSolicitud(@RequestBody BandejaDocSFM bandejaDocSFM){

      bandejaDocSFM.setEtapa(Etapa.of().idEtapa(Etapas.RECEPCION).get());
      this.service.save(bandejaDocSFM);

      List<BandejaDocSFM> bandejaDocSFMDb = this.service.findAll();
      if (bandejaDocSFMDb.size() == 0) throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_SAVE("Solicitud"))
                                       .data(bandejaDocSFMDb)
                                       .build());
   }

   @GetMapping( path = { "/findAllTipoTramite" } )
   public ResponseEntity<?> findAllTipoTramite() {
       return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(this.tipoTramiteService.findAll())
                                       .build());
   }

   @GetMapping( path = { "/findAllBandejaEntrada" } )
   public ResponseEntity<?> findAllBandejaEntrada(){
      List<BandejaDocSFM> bandejaDocSFMDb = this.service.findAll();
      if(bandejaDocSFMDb.size() == 0) throw new DataAccessEmptyWarning();
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                                       .data(bandejaDocSFMDb)
                                       .build());
   }

   @DeleteMapping( path = { "/deleteSolicitud/{idBandejaDoc}" } )
   public ResponseEntity<?> deleteSolicitud(@PathVariable Long idBandejaDoc) {

      this.service.deleteById(idBandejaDoc);

      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_DELETE_RECORD(idBandejaDoc))
                                       .data(this.service.findAll())
                                       .build());
   }

   @PostMapping( path = { "/assignEvaluador" } )
   public ResponseEntity<?> assignEvaluador(@RequestBody EvaluarSolicitudSFM evaluarSolicitudSFM) {
       
      BandejaDocSFM bandejaDocSFM =  this.service
                                             .findById(evaluarSolicitudSFM.getBandejaDoc().getIdBandejaDoc())
                                             .orElseThrow(DataAccessEmptyWarning::new);
      Usuario operadorDesig = evaluarSolicitudSFM.getOperadorDesig();

      boolean inEvaluacion = bandejaDocSFM.getEtapa().getIdEtapa() == Etapas.EVALUACION;

      if(!inEvaluacion){
         /*» Nuevo: Evaluador ...  */
         EvaluarSolicitudSFM evaluarSolicitudSFMNew = EvaluarSolicitudSFM
                                                         .of()
                                                         .operadorDesig(operadorDesig)
                                                         .get();
         /*» Actualiza: Etapa ...  */
         bandejaDocSFM.setEtapa(Etapa.of().idEtapa(Etapas.EVALUACION).get());
         bandejaDocSFM.setEvaluarSolicitud(evaluarSolicitudSFMNew);
      } else {
         /*» Actualiza: Operador designado ...  */
         bandejaDocSFM.getEvaluarSolicitud().setOperadorDesig(evaluarSolicitudSFM.getOperadorDesig());
      }

      /*» Save ... */
      this.service.save(bandejaDocSFM);
       
      return ResponseEntity.ok().body(
                                    Response
                                       .builder()
                                       .message(Messages.SUCCESS_SAVE_TO_ASSIGN_REVISOR(operadorDesig.getNombres()))
                                       .data(this.service.findAll())
                                       .build());
   }
 

}