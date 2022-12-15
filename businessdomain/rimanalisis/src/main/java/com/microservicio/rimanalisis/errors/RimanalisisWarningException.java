package com.microservicio.rimanalisis.errors;

import com.commons.utils.constants.LevelLog;

public class RimanalisisWarningException extends RuntimeException {

   public RimanalisisWarningException(String message) {
      super(message);
      System.setProperty(LevelLog.WARNING, LevelLog.WARNING);
   }

}
