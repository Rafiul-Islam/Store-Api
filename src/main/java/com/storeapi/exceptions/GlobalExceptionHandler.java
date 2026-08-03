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
  public ResponseEntity<String> handleBadCredentials(BadCredentialsException exception) {
    log.error("BadCredentialsException: {}", exception.getMessage());
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({CartNotFoundException.class, EmptyCartException.class})
  public ResponseEntity<Map<String, String>> handleCartExceptions(EmptyCartException exception) {
    log.error("CartException: {}", exception.getMessage());
    return ResponseEntity.badRequest().body(
      Map.of("error", exception.getMessage())
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
    Map<String, String> exceptions = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach((err) -> {
      String fieldName = ((FieldError) err).getField();
      String errorMessage = err.getDefaultMessage();
      exceptions.put(fieldName, errorMessage);
    });
    log.error("{}", exceptions);
    return ResponseEntity.badRequest().body(exceptions);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException exception) {
    log.error("HttpMessageNotReadableException: {}", exception.getMessage());
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(Map.of("error", "Invalid request parameters"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleExceptions(Exception exception) {
    log.error("Exception: {}", exception.getMessage());
    return ResponseEntity.badRequest().body(
      Map.of("error", exception.getMessage()).toString()
    );
  }
}
