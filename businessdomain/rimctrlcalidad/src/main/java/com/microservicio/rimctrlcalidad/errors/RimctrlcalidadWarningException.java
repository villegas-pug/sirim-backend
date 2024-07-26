package com.microservicio.rimctrlcalidad.errors;

import com.commons.utils.constants.MessageType;

public class RimctrlcalidadWarningException extends RuntimeException {

   public RimctrlcalidadWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
