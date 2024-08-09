package com.commons.utils.utils;

import com.commons.utils.constants.MessageType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
   private @Builder.Default String messageType = MessageType.SUCCESS;
   private String message = "";
   private @Builder.Default T data = null;
}