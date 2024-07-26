
package com.commons.utils.errors;

import com.commons.utils.constants.MessageType;

public class AsignWarning extends RuntimeException{

   public AsignWarning(String msj) {
      super(msj);
      System.setProperty("levelLog", MessageType.WARNING);
   }

}