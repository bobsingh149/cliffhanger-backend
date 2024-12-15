package com.example.barter.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.barter.exception.customexception.EmptyQueryException;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.exception.customexception.OpenLibraryBooksApiException;
import com.example.barter.exception.customexception.UserNotFoundException;
import com.example.barter.utils.ControllerUtils;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(OpenLibraryBooksApiException.class)
    public ResponseEntity<ErrorResponse> handleGoogleBooksApiException(OpenLibraryBooksApiException e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage()!= null ? e.getMessage() : "cannot get the search results")
                .status(ControllerUtils.ResponseMessage.failure)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage()!= null ? e.getMessage() : "internal server error")
                .status(ControllerUtils.ResponseMessage.failure)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyQueryException.class)
    public ResponseEntity<ErrorResponse> handleEmptyQueryException(EmptyQueryException e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage()!= null ? e.getMessage() : "empty query is not allowed")
                .status(ControllerUtils.ResponseMessage.failure)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageUploadFailed.class)
    public ResponseEntity<ErrorResponse> handleImageUploadFailedException(ImageUploadFailed e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage()!= null ? e.getMessage() : "image upload failed")
                .status(ControllerUtils.ResponseMessage.failure)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage() != null ? e.getMessage() : "user not found")
                .status(ControllerUtils.ResponseMessage.failure)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
