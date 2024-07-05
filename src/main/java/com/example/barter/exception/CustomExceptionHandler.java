package com.example.barter.exception;


import com.example.barter.exception.customexception.InvalidIsbnException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(InvalidIsbnException.class)
    public ResponseEntity<ErrorResponse> handleInvalidIsbnException(InvalidIsbnException ex)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage()).build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage()).build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
