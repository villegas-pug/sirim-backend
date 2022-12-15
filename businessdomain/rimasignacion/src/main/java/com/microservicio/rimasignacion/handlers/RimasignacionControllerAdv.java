package com.microservicio.rimasignacion.handlers;

import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.handlers.CommonControllerAdv;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimasignacion.errors.RimasignacionWarningException;
import com.microservicio.rimasignacion.services.RimasignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimasignacionControllerAdv extends CommonControllerAdv {
   
   @Autowired
   private RimasignacionService rimasignacionService;
   
   @ExceptionHandler(value = { RimasignacionWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> rimasignacionWarningException(Exception e){

      return Response
                  .<List<TablaDinamicaDto>>builder()
                  .levelLog(LevelLog.WARNING)
                  .message(e.getMessage())
                  .data(this.rimasignacionService.findAllTablaDinamica())
                  .build();

   }

}
