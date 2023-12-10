package com.mogilan.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handlerException(Exception exception) {
        var responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);

        if (exception instanceof EntityNotFoundException) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            responseEntity = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED);
        } else if (exception instanceof RuntimeException) {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
