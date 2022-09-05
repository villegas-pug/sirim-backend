package com.microservicio.rimextraccion.handlers;

import java.util.List;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.handlers.CommonControllerAdv;
import com.commons.utils.utils.LogAndResponse;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.constants.RimHttpHeaders;
import com.microservicio.rimextraccion.errors.NotFoundDownloadException;
import com.microservicio.rimextraccion.errors.RimcommonWarningException;
import com.microservicio.rimextraccion.errors.RimdepurarExtraccionWarningException;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.services.RimcommonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimextraccionControllerAdv extends CommonControllerAdv {

   @Autowired
   private RimcommonService commonService;

   @ExceptionHandler( value = { RimcommonWarningException.class } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<Object> rimcommonWarningException(Exception e){
      return LogAndResponse.handleLogAndResponse(e.getMessage());
   }

   @ExceptionHandler(value = { NotFoundDownloadException.class })
   public ResponseEntity<?> notFoundDownloadException(Exception e){
      HttpHeaders headers = new HttpHeaders();
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.WARNING);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGGE_WARNING_EMPTY);
            
      return ResponseEntity
                  .status(HttpStatus.OK)
                  .headers(headers)
                  .body(null);
   }

   @ExceptionHandler(value = { RimdepurarExtraccionWarningException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TablaDinamicaDto>> depurarExtraccionWarningException(Exception e){
      return Response
               .<List<TablaDinamicaDto>>builder()
               .levelLog(System.getProperty(LevelLog.WARNING))
               .message(e.getMessage())
               .data(this.commonService.findAllTablaDinamica())
               .build();

   }
   
}
