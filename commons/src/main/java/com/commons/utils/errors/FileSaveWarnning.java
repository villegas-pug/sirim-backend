package com.commons.utils.errors;

import com.commons.utils.constants.*;

public class FileSaveWarnning extends RuntimeException {
   public FileSaveWarnning(String fileName){
      super(Messages.MESSAGE_WARNING_FILE_SAVE(fileName));
      System.setProperty(props.LEVELLOG, LevelLog.WARNING);
   }
   
}
