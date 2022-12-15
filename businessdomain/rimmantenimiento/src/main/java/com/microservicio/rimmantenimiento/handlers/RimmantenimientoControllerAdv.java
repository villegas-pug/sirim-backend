package com.microservicio.rimmantenimiento.handlers;

import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.handlers.CommonControllerAdv;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimmantenimiento.errors.EventoWarningException;
import com.microservicio.rimmantenimiento.errors.RimmantenimientoWarningException;
import com.microservicio.rimmantenimiento.services.RimmantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimmantenimientoControllerAdv extends CommonControllerAdv {

   @Autowired
   private RimmantenimientoService rimmantenimientoService;
   
   @ExceptionHandler(value = { RimmantenimientoWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> test(Exception e){
      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .levelLog(LevelLog.WARNING)
                  .message(Messages.MESSAGGE_ERROR_DATA_ACCESS)
                  .data(rimmantenimientoService.findAllTablaDinamica())
                  .build();
   }

   @ExceptionHandler(value = { EventoWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<?>> rimeventoWarningException(Exception e){
      return Response
               .<List<?>>builder()
               .levelLog(System.getProperty(LevelLog.WARNING))
               .message(e.getMessage())
               .data(List.of())
               .build();

   }

}
