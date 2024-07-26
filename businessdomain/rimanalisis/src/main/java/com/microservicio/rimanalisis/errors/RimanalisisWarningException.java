package com.microservicio.rimanalisis.errors;

import com.commons.utils.constants.MessageType;

public class RimanalisisWarningException extends RuntimeException {

   public RimanalisisWarningException(String message) {
      super(message);
      System.setProperty(MessageType.WARNING, MessageType.WARNING);
   }

}
