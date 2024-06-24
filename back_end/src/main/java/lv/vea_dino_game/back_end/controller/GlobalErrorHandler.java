package lv.vea_dino_game.back_end.controller;

import java.util.*;

import lv.vea_dino_game.back_end.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lv.vea_dino_game.back_end.model.dto.ErrorResponse;


@RestControllerAdvice
public class GlobalErrorHandler {

  private final String TYPE = "INVALID_INPUT";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {


    String name = "User input validation error";
    String message = "The user input you have provided is invalid. See the detailed info:";
    HashMap<String, String> validationErrors = new HashMap<>();

    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    return new ResponseEntity<>(new ErrorResponse(TYPE, name, message, validationErrors),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {

    String type = "INVALID_INPUT";
    String name = "User input validation error";
    String message = "The user input you have provided is invalid. See the detailed info:";
    HashMap<String, String> validationErrors = new HashMap<>();

    e.getConstraintViolations().forEach(error -> {

      String violatedColumnName = error.getPropertyPath().toString();
      String formattedFieldName = Character.toString(Character.toUpperCase(violatedColumnName.charAt(0)))
          + violatedColumnName.substring(1);
      validationErrors.put(formattedFieldName, error.getMessage());
    });

    return new ResponseEntity<ErrorResponse>(new ErrorResponse(type, name, message, validationErrors),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    return new ResponseEntity<>(new ErrorResponse(TYPE, "User input validation error", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException e) {
    return new ResponseEntity<>(
        new ErrorResponse("REQ_BODY_ERR", "Expected request body missing",
            "Expected request body required for this action is missing", null),
        HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(InvalidAuthenticationDataException.class)
  public ResponseEntity<ErrorResponse> handleInvalidAuthInput(InvalidAuthenticationDataException e) {
    return new ResponseEntity<>(
        new ErrorResponse("AUTH_ERR", "Unable to authenticate", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ServiceCurrentlyUnavailableException.class)
  public ResponseEntity<ErrorResponse> handleCurrentlyUnavailable(ServiceCurrentlyUnavailableException e) {
    return new ResponseEntity<>(
        new ErrorResponse("INT_SERV_ERR", "Currently unable to perform this action.", e.getMessage(), null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidClanException.class)
  public ResponseEntity<ErrorResponse> handleInvalidClanException(InvalidClanException e) {
    return new ResponseEntity<>(new ErrorResponse(TYPE, "Clan input validation error", e.getMessage(), null),
            HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidPlayerException.class)
  public ResponseEntity<ErrorResponse> handleUserInvalidPlayerException(InvalidPlayerException e) {
    return new ResponseEntity<>(
        new ErrorResponse(TYPE, "Player input validation error", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchUserException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchUserException(NoSuchUserException e) {
    return new ResponseEntity<>(
        new ErrorResponse(TYPE, "No such user", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidUserInputException.class)
  public ResponseEntity<ErrorResponse> handleInvalidUserInputException(InvalidUserInputException e) {
    return new ResponseEntity<>(
        new ErrorResponse(TYPE, "User input invalid", e.getMessage(), null),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> handleInvalidUserInputException(AuthorizationDeniedException e) {
    return new ResponseEntity<>(
        new ErrorResponse("AUTHORIZ_ERR", "Authorisation denied", "You have no permission to access this resource", null),
        HttpStatus.BAD_REQUEST);
  }

  // Keep this at the end for all the uncaught errors
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleAnyOtherException(Exception e) {
    System.out.println("💥💥💥💥 -----> " + e.getClass().getSimpleName());
    System.out.println(e.getMessage());
    return new ResponseEntity<>(new ErrorResponse("INT_SERV_ERR", "Service unuvailable", e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
