package lv.vea_dino_game.back_end.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lv.vea_dino_game.back_end.exceptions.InvalidAuthenticationDataException;
import lv.vea_dino_game.back_end.model.dto.ErrorResponse;


@RestControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

    String type = "INVALID_INPUT";
    String name = "User input validation error";
    String message = "The user input you have provided is invalid. See the detailed info:";
    HashMap<String, String> validationErrors = new HashMap<>();

    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    return new ResponseEntity<ErrorResponse>(new ErrorResponse(type, name, message, validationErrors),
        HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException e) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse("REQ_BODY_ERR", "Expected request body missing",
            "Expected request body required for this action is missing", null),
        HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(InvalidAuthenticationDataException.class)
  public ResponseEntity<ErrorResponse> handleInvalidAuthInput(InvalidAuthenticationDataException e) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse("AUTH_ERR", "Unable to authenticate", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }
  
  // PLACE YOURHANDLER BELOW HERE:
  

  // Keep this at the end for all the uncaught errors
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleAnyOtherException(Exception e) {
    System.out.println("💥💥💥💥 -----> " + e.getClass().getSimpleName());
    return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
