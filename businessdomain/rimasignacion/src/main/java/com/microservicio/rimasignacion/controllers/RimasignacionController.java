package com.microservicio.rimasignacion.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimasignacion.errors.RimasignacionWarningException;
import com.microservicio.rimasignacion.services.RimasignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@CrossOrigin(origins = { "*" })
@RestController
public class RimasignacionController {
   
   @Autowired
   private RimasignacionService rimasignacionService;
   
   @PostMapping(path = { "/assignedToGrupoAnalisis" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> assignedToGrupoAnalisis(@RequestBody(required = false) AsigGrupoCamposAnalisis asigGrupoCamposAnalisis) {

      // ► Dep's ...
      int rangeIni = asigGrupoCamposAnalisis.getRegAnalisisIni(),
          rangeFin = asigGrupoCamposAnalisis.getRegAnalisisFin();
          
      // ► Validación: Si no existe usuario ...
      if(asigGrupoCamposAnalisis.getUsrAnalista().getIdUsuario() == null)
         throw new RimasignacionWarningException(Messages.WARNING_USER_NOT_EXISTS);

      // ► Validación: Mínimo de registros permitos es 2 ...
      if((rangeFin - rangeIni) < 1)
         throw new RimasignacionWarningException(Messages.WARNING_MIN_RECORDS_TO_ASSIGN(2));

      // ► Repo dep's ...
      GrupoCamposAnalisisDto grupoAnalisis = this.rimasignacionService
                                                         .findGrupoCamposAnalisisById(asigGrupoCamposAnalisis.getGrupo()
                                                         .getIdGrupo());

      String nombreTabla = grupoAnalisis.getTablaDinamica()
                                        .getNombre();

      int totalRegExtraccion = this.rimasignacionService.countTablaByNombre(nombreTabla).intValue();
      if(totalRegExtraccion == 0) throw new RimasignacionWarningException(Messages.WARNING_RECORDS_NOT_FOUND_TO_SEGMENT);

      // ► Correlativo iniciado en 0 e incrementado en 1, a partir de un valor máximo ...
      String rangesAvailables = this.convertArrIntToStr(this.generateRangeNumbersToArr(totalRegExtraccion));

      // ► Rangos asigandos en el grupo ...
      for (AsigGrupoCamposAnalisisDto asign : grupoAnalisis.getAsigGrupoCamposAnalisis()) {

         // ► Rangos asignados ...
         String rangoAsignedBefore = this.convertArrIntToStr(
                                                this.generateRangeNumbersToArr(asign.getRegAnalisisIni(), asign.getRegAnalisisFin()));

         // ► Reemplaza `Rangos asignados` en `rangeExtraccionStr` ...
         rangesAvailables = rangesAvailables.replace(rangoAsignedBefore, "").replace(", ,", ",");
      }

      // ► Validar: Si el rango enviado está disponible ...
      String rangeAssigned = this.convertArrIntToStr(this.generateRangeNumbersToArr(rangeIni, rangeFin));
      boolean isAvailableRange = rangesAvailables.contains(rangeAssigned);

      // ► Save: ...
      if (isAvailableRange) this.rimasignacionService.save(asigGrupoCamposAnalisis);
      else throw new RimasignacionWarningException(Messages.WARNING_ASIGN_REG_ANALISIS);

      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_ASIGN_REG_ANALISIS)
               .data(List.of())
               .build();
   }

   @DeleteMapping(path = { "/deleteAssignedToGrupoAById/{idAsign}" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> deleteAssignedToGrupoAById(@PathVariable Long idAsign) {
      this.rimasignacionService.deleteById(idAsign);
      return Response
                  .<List<?>>builder()
                  .message(Messages.MESSAGE_SUCCESS_DELETE_BY_ID(idAsign))
                  .data(List.of())
                  .build();
   }

   @PutMapping( path = { "/reasignToGrupoAnalisis" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> reasignToGrupoAnalisis(@RequestBody AsigGrupoCamposAnalisis reasign) {

      /*► Dep's ... */
      Long idAsigGrupo = reasign.getIdAsigGrupo();
      Usuario usrAnalistaNew = reasign.getUsrAnalista();

      // ► Repo dep's ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasignacionService.findById(idAsigGrupo);

      // ► Validación: Si no existe usuario ...
      if(usrAnalistaNew.getIdUsuario() == null)
         throw new RimasignacionWarningException(Messages.WARNING_USER_NOT_EXISTS);

      /*► Update and Save ...  */
      asigGrupoCamposAnalisis.setUsrAnalista(usrAnalistaNew);
      asigGrupoCamposAnalisis.setFechaAsignacion(new Date());
      this.rimasignacionService.save(asigGrupoCamposAnalisis);

      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_SAVE_TO_ASSIGN_REVISOR(usrAnalistaNew.getNombres()))
               .data(List.of())
               .build();
   }

   //#region Private method's ...

   private int[] generateRangeNumbersToArr(int... params) {/*► (tamaño, rangoIni, rangoFin) */

      int countParam = params.length;
      int rangoFin = 0,
          rangoIni = 1;

      switch (countParam) {
         case 1:
            rangoFin = params[0];
            break;
         case 2:
            rangoFin = (params[1] - params[0]) + 1;
            rangoIni = params[0];
            break;
      }

      int[] consecutiveArr = new int[rangoFin];
      for (int i = 0; i < rangoFin; i++) {
         consecutiveArr[i] = rangoIni + i;
      }

      return consecutiveArr;
   }

   private String convertArrIntToStr(int[] intArray) {
      String arrStr = Arrays.toString(intArray);
      return arrStr.substring(1, arrStr.length() - 1);
   }

   //#endregion

}
