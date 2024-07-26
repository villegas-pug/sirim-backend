package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.MessageType;

public class RimdepurarExtraccionWarningException extends RuntimeException {

   public RimdepurarExtraccionWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }
   
}
