package com.microservicio.rimctrlcalidad.errors;

import com.commons.utils.constants.LevelLog;

public class RimctrlcalidadWarningException extends RuntimeException {

   public RimctrlcalidadWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
