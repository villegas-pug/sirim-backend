package com.microservicio.rimasignacion.errors;

import com.commons.utils.constants.LevelLog;

public class RimasignacionWarningException extends RuntimeException {

   public RimasignacionWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
