package com.microservicios.operativo.controllers;

import java.util.List;

import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.AuditoriaConsultaData;
import com.commons.utils.models.entities.Refugiado;
import com.commons.utils.utils.Response;
import com.microservicios.operativo.services.AuditoriaConsultaDataService;
import com.microservicios.operativo.services.RefugiadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "*" })
@RestController
public class RefugiadoController {

   @Autowired
   private RefugiadoService refugiadoService;

   @Autowired
   private AuditoriaConsultaDataService auditoriaConsultaDataService;

   @PostMapping(path = "/findByRefugiado")
   public ResponseEntity<?> findByRefugiado(@RequestBody RefugiadoDto refugiadoDto) {

      /*→ SidAuditoriaConsultaData ... */
      String payload = refugiadoDto.refugiado.toString();
      refugiadoDto.consultaData.setPayload(payload);
      this.auditoriaConsultaDataService.save(refugiadoDto.consultaData);

      /*→ SidRefugiado ...   */
      List<Refugiado> refugiadoDb = 
         this.refugiadoService.findByCustomFilter(
            refugiadoDto.refugiado.getNombres(), 
            refugiadoDto.refugiado.getPaterno(), 
            refugiadoDto.refugiado.getMaterno());
         
      if(refugiadoDb.size() == 0)
         throw new DataAccessEmptyWarning();

      return ResponseEntity.ok().body(
            Response
               .builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(refugiadoDb)
               .build()
         );
   }
   
   public static class RefugiadoDto {
      public Refugiado refugiado;
      public AuditoriaConsultaData consultaData;
   }
   

}
