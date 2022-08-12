package com.microservicio.rimextraccion.handlers;

import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.handlers.CommonControllerAdv;
import com.microservicio.rimextraccion.constants.RimHttpHeaders;
import com.microservicio.rimextraccion.errors.NotFoundDownloadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RimextraccionControllerAdv extends CommonControllerAdv {

   @ExceptionHandler(value = { NotFoundDownloadException.class })
   public ResponseEntity<?> notFoundDownloadException(Exception e){
      HttpHeaders headers = new HttpHeaders();
      headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.WARNING);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGGE_WARNING_EMPTY);
            
      return ResponseEntity
                  .status(HttpStatus.ACCEPTED)
                  .headers(headers)
                  .body(null);
   }
   
}
