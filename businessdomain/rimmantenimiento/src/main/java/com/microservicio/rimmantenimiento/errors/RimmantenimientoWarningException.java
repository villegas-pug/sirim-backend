package com.microservicio.rimmantenimiento.errors;

import com.commons.utils.constants.LevelLog;

public class RimmantenimientoWarningException extends RuntimeException {

   public RimmantenimientoWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
