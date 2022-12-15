package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.LevelLog;

public class RimeventoWarningException extends RuntimeException {

   public RimeventoWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
