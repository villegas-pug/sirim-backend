package com.microservicio.rimextraccion.errors;

import com.commons.utils.constants.MessageType;

public class RimcommonWarningException extends RuntimeException {

   public RimcommonWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
