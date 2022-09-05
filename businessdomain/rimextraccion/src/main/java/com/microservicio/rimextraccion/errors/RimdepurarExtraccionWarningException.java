package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.LevelLog;

public class RimdepurarExtraccionWarningException extends RuntimeException {

   public RimdepurarExtraccionWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }
   
}
