package org.edu.kiu.midterm.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.service.InternationalizedMessageResolver;
import org.edu.kiu.midterm.util.InternationalizedMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final InternationalizedMessageResolver messageResolver;

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
    log.warn("handleEntityNotFound:: Resource not found: {}", ex.getMessage());
    var message = messageResolver.resolve(InternationalizedMessages.ERROR_NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
    log.warn("handleAccessDenied:: Access denied for request");
    var message = messageResolver.resolve(InternationalizedMessages.ERROR_ACCESS_DENIED);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      var fieldName = ((FieldError) error).getField();
      var errorMessage = resolveValidationMessage(error.getDefaultMessage());
      errors.put(fieldName, errorMessage);
    });
    log.debug("handleValidationExceptions:: Validation failed with {} field error(s)", errors.size());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    log.error("handleGeneralException:: Unhandled exception", ex);
    var message = messageResolver.resolve(InternationalizedMessages.ERROR_GENERAL);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }

  private String resolveValidationMessage(String defaultMessage) {
    return defaultMessage != null && defaultMessage.matches("\\{.+}")
        ? messageResolver.resolve(defaultMessage.substring(1, defaultMessage.length() - 1), defaultMessage)
        : defaultMessage;
  }

}
