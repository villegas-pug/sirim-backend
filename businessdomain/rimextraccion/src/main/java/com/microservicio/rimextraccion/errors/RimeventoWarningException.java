package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.MessageType;

public class RimeventoWarningException extends RuntimeException {

   public RimeventoWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
