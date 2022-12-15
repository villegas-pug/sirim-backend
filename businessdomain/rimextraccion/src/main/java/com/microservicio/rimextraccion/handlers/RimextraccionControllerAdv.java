package com.microservicio.rimextraccion.handlers;

import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.handlers.CommonControllerAdv;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.errors.RimcommonWarningException;
import com.microservicio.rimextraccion.errors.RimdepurarExtraccionWarningException;
import com.microservicio.rimextraccion.services.RimextraccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimextraccionControllerAdv extends CommonControllerAdv {

   @Autowired
   private RimextraccionService rimextraccionService;

   @ExceptionHandler(value = { RimcommonWarningException.class, RimdepurarExtraccionWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> rimcommonWarningException(Exception e){

      List<TablaDinamicaDto> tablaDinamicaDb = this.rimextraccionService.findAllTablaDinamica();

      return Response
               .<List<TablaDinamicaDto>>builder()
               .levelLog(System.getProperty(LevelLog.WARNING))
               .message(e.getMessage())
               .data(tablaDinamicaDb)
               .build();
   }
  
   
}
