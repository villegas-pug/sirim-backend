package com.commons.utils.handlers;

import java.util.List;

import com.commons.utils.constants.MessageType;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.RimHttpHeaders;
import com.commons.utils.errors.AsignWarning;
import com.commons.utils.errors.CreateTableWarning;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.EntityFindByIdWarning;
import com.commons.utils.errors.FileSaveWarnning;
import com.commons.utils.errors.NotFoundDownloadException;
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
   protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
      return new ResponseEntity<>(handleResponse(Messages.MESSAGGE_ERROR_DATA_ACCESS(), MessageType.ERROR), status);
   }

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
      return new ResponseEntity<>(handleResponse(Messages.MESSAGGE_ERROR_DATA_ACCESS(), MessageType.ERROR), status);
   }

   // #endregion

   // #region : Custom Exception

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ DataAccessEmptyWarning.class })
   public Response<Object> handlerNotFound(DataAccessEmptyWarning e) {
      return handleResponse(e.getMessage(), MessageType.WARNING);
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ EntityFindByIdWarning.class, UserNotFoundWarning.class })
   public Response<Object> handleDataAccessError(Exception e) {
      return handleResponse(e.getMessage(), MessageType.WARNING);
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ Exception.class, DataAccessException.class })
   public Response<Object> handleDataAccessException(Exception e) {
      return handleResponse(e.getMessage(), MessageType.WARNING);
   }

   @ResponseStatus(HttpStatus.OK)
   @ExceptionHandler({ FileSaveWarnning.class })
   public Response<Object> handleSaveException(Exception e){
      return handleResponse(e.getMessage(), MessageType.WARNING);
   }
   
   @ResponseStatus(code = HttpStatus.OK)
   @ExceptionHandler({ CreateTableWarning.class, AsignWarning.class })
   public Response<Object> handleCreateTableException(Exception e){
      return handleResponse(e.getMessage(), MessageType.WARNING);
   }

   @ExceptionHandler(value = { NotFoundDownloadException.class })
   @ResponseStatus(value = HttpStatus.OK)
   public ResponseEntity<?> notFoundDownloadException(Exception e){
      HttpHeaders headers = new HttpHeaders();
      headers.add(RimHttpHeaders.RESPONSE_STATUS, MessageType.WARNING);
      headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGGE_WARNING_EMPTY);
            
      return ResponseEntity
                  .status(HttpStatus.OK)
                  .headers(headers)
                  .body(List.of());
   }

   // endregion

   public Response<Object> handleResponse(String msjLog, String messageType) {
      return Response
               .builder()
                  .messageType(messageType)
                  .message(msjLog)
                  .data(List.of())
                  .build();
   }

}