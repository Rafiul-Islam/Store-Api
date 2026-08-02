package com.storeapi.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
    Map<String, String> exceptions = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach((err) -> {
      String fieldName = ((FieldError) err).getField();
      String errorMessage = err.getDefaultMessage();
      exceptions.put(fieldName, errorMessage);
    });
    return ResponseEntity.badRequest().body(exceptions);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
    log.error("JSON parse error: {}", ex.getMessage());
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(Map.of("error", "Invalid request parameters"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleExceptions(Exception exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
