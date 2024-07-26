package com.microservicio.rimmantenimiento.errors;

import com.commons.utils.constants.MessageType;

public class EventoWarningException extends RuntimeException {

   public EventoWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
