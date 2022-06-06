package com.commons.utils.errors;

import com.commons.utils.constants.*;

public class DataAccessEmptyWarning extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public DataAccessEmptyWarning() {
      super(Messages.MESSAGGE_WARNING_EMPTY());
      System.setProperty("levelLog", LevelLog.WARNING);
   }
}