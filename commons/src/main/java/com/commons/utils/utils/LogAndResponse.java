package com.commons.utils.utils;

import java.util.List;
import java.util.UUID;
import com.commons.utils.constants.MessageType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogAndResponse {

   public static Response<Object> handleLogAndResponse(String msjResponse, String msjLog, String error) {
      String microservicio = System.getProperty("microservicio");
      String method = System.getProperty("method");
      String levelLog = System.getProperty("levelLog");
      String logMsj = String.format("%s, microservicio: %s, method: %s(), message: %s, error: %s", getIdLog(),
            microservicio, method, msjLog, error);
      switch (levelLog) {
         case MessageType.INFO:
            log.info(logMsj);
            break;
         case MessageType.WARNING:
            log.warn(logMsj);
            break;
         case MessageType.ERROR:
            log.error(logMsj);
            break;
         default:
            log.error(logMsj);
            break;
      }
      return Response
               .builder()
               .messageType(levelLog)
               .data(List.of())
               .message(msjResponse)
               .build();
   }
   
   public static Response<Object> handleLogAndResponse(String msjLog) {
      String levelLog = System.getProperty(MessageType.WARNING);
      String logMsj = String.format("%s, message: %s", getIdLog(), msjLog);
      switch (levelLog) {
         case MessageType.INFO:
            log.info(logMsj);
            break;
         case MessageType.WARNING:
            log.warn(logMsj);
            break;
         case MessageType.ERROR:
            log.error(logMsj);
            break;
         default:
            log.error(logMsj);
            break;
      }
      return Response
               .builder()
               .messageType(levelLog)
               .message(msjLog)
               .build();
   }

   public static String getIdLog() {
      return UUID.randomUUID().toString();
   }
}