package com.commons.utils.errors;

import com.commons.utils.constants.*;

public class EntityFindByIdWarning extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public EntityFindByIdWarning(long id) {
      super(Messages.MESSAGE_WARNING_ENTITY_FIND_BY_ID(id));
      System.setProperty(props.LEVELLOG, MessageType.WARNING);
   }

}