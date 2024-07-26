package com.commons.utils.errors;

import com.commons.utils.constants.*;

public class UserNotFoundWarning extends RuntimeException {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public UserNotFoundWarning(String login) {
      super(Messages.MESSAGE_WARNING_USER_NOTFOUND(login));
      System.setProperty("levelLog", MessageType.WARNING);
   }

}
