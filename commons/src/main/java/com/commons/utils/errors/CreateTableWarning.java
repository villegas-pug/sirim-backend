package com.commons.utils.errors;

import com.commons.utils.constants.MessageType;

public class CreateTableWarning extends RuntimeException {

   public CreateTableWarning(String msg) {
      super(msg);
      System.setProperty("levelLog", MessageType.WARNING);
   }
   
}
