package com.sportygroup.ticketing.application.rest.common;

import com.sportygroup.ticketing.domain.exception.InvalidStatusTransitionException;
import com.sportygroup.ticketing.domain.exception.TicketNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String ERROR_MESSAGE = "error";

  @ExceptionHandler(InvalidStatusTransitionException.class)
  public ResponseEntity<Map<String, String>> handleInvalidTransition(InvalidStatusTransitionException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_MESSAGE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(TicketNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(TicketNotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_MESSAGE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
      HttpMessageNotReadableException.class})
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_MESSAGE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
    Map<String, String> error = new HashMap<>();
    log.error(ex.getMessage(), ex);
    error.put(ERROR_MESSAGE, "Unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}