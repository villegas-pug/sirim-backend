package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.LevelLog;

public class RimcommonWarningException extends RuntimeException {

   public RimcommonWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
