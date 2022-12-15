package com.microservicio.rimmantenimiento.errors;

import com.commons.utils.constants.LevelLog;

public class EventoWarningException extends RuntimeException {

   public EventoWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
