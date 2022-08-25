package com.commons.utils.utils;

import com.commons.utils.constants.LevelLog;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T> {
   private @Builder.Default String levelLog = LevelLog.SUCCESS;
   private String message;
   private @Builder.Default T data = null;
}