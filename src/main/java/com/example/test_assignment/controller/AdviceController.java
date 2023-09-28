package com.example.test_assignment.controller;

import com.example.test_assignment.model.ApiError;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    log.warn("Errors of validation: {}", errors);
    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors, ex));
  }

  @ExceptionHandler()
  protected ResponseEntity<Object> handleNullPointerException(Exception ex) {
    log.warn("Exception: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
  }

}
