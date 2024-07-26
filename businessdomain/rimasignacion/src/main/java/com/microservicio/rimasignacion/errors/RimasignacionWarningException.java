package com.microservicio.rimasignacion.errors;

import com.commons.utils.constants.MessageType;

public class RimasignacionWarningException extends RuntimeException {

   public RimasignacionWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
