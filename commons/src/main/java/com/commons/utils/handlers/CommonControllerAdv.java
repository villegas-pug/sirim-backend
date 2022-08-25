package com.commons.utils.handlers;

import com.commons.utils.constants.Messages;
import com.commons.utils.errors.AsignWarning;
import com.commons.utils.errors.CreateTableWarning;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.EntityFindByIdWarning;
import com.commons.utils.errors.FileSaveWarnning;
import com.commons.utils.errors.UserNotFoundWarning;
import com.commons.utils.utils.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CommonControllerAdv extends ResponseEntityExceptionHandler {

   // #region: Internal Exceptión
   @Override
   protected ResponseEntity<Object> handleExceptionInternal(
         Exception ex, Object body, HttpHeaders headers,
         HttpStatus status, WebRequest request) {
      return new ResponseEntity<>(handleInternalResponseException(status, ex), status);
   }

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
      return new ResponseEntity<>(handleInternalResponseException(status, ex), status);
   }

   private Response<Object> handleInternalResponseException(HttpStatus status, Exception ex) {
      return LogAndResponse.handleLogAndResponse(
                  Messages.MESSAGGE_ERROR_DATA_ACCESS(), 
                  ex.getMessage(),
                  status.getReasonPhrase());
   }
   // #endregion

   // #region : Custom Exception

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ DataAccessEmptyWarning.class })
   public Response<Object> handlerNotFound(DataAccessEmptyWarning e) {
      String msjResponse = e.getMessage();
      return LogAndResponse.handleLogAndResponse(msjResponse, msjResponse, null);
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ EntityFindByIdWarning.class, UserNotFoundWarning.class })
   public Response<Object> handleDataAccessError(Exception e) {
      String msjResponse = e.getMessage();
      return LogAndResponse.handleLogAndResponse(msjResponse, msjResponse, null);
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({Exception.class, DataAccessException.class, NullPointerException.class })
   public Response<Object> handleDataAccessException(Exception e) {
      String msjResponse = Messages.MESSAGGE_ERROR_DATA_ACCESS();
      return LogAndResponse.handleLogAndResponse(msjResponse, e.getMessage(), e.toString());
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ FileSaveWarnning.class })
   public Response<Object> handleSaveException(Exception e){
      return LogAndResponse.handleLogAndResponse(e.getMessage(), e.getMessage(), null);
   }
   
   @ResponseStatus(code = HttpStatus.OK)
   @ExceptionHandler({ CreateTableWarning.class, AsignWarning.class })
   public Response<Object> handleCreateTableException(Exception e){
      return LogAndResponse.handleLogAndResponse(e.getMessage(), e.getMessage(), null);
   }

   // endregion

}