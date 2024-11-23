package com.example.barter.exception;


import com.example.barter.exception.customexception.EmptyQueryException;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.exception.customexception.OpenLibraryBooksApiException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(OpenLibraryBooksApiException.class)
    public ResponseEntity<ErrorResponse> handleGoogleBooksApiException(OpenLibraryBooksApiException e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(e.getMessage()!= null ?e.getMessage() :"cannot get the search results").build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(e.getMessage()!= null ?e.getMessage() :"internal server error").build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyQueryException.class)
    public ResponseEntity<ErrorResponse> handleEmptyQueryException(EmptyQueryException e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(e.getMessage()!= null ?e.getMessage() :"empty query is not allowed").build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageUploadFailed.class)
    public ResponseEntity<ErrorResponse> handleImageUploadFailedException(ImageUploadFailed e)
    {
        final ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(e.getMessage()!= null ? e.getMessage() :"image upload failed").build();
        return  new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
