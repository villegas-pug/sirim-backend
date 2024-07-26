package com.microservicio.rimmantenimiento.errors;

import com.commons.utils.constants.MessageType;

public class RimmantenimientoWarningException extends RuntimeException {

   public RimmantenimientoWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
