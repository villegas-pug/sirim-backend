package com.microservicio.rimctrlcalidad.handlers;

import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.handlers.CommonControllerAdv;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimctrlcalidad.errors.RimctrlcalidadWarningException;
import com.microservicio.rimctrlcalidad.services.RimctrlCalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimctrlcalidadControllerAdv extends CommonControllerAdv {

   @Autowired
   private RimctrlCalidadService rimctrlCalidadService;
   
   @ExceptionHandler(value = { RimctrlcalidadWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> rimasignacionWarningException(Exception e){

      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .levelLog(LevelLog.WARNING)
                  .message(e.getMessage())
                  .data(this.rimctrlCalidadService.findAllTablaDinamica())
                  .build();

   }
   
}
