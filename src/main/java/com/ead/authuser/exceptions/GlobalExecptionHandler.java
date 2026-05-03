package com.ead.authuser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(NotFoundExcepiton.class)
    public ResponseEntity<ErrorRecordResponse> handleNotFoundExcepiton(NotFoundExcepiton ex) {
        ErrorRecordResponse errorRecordResponse = new ErrorRecordResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRecordResponse);
    }

    @ExceptionHandler(ErrorConflitException.class)
    public ResponseEntity<ErrorRecordResponse> handleErrorConflitException(ErrorConflitException ex) {
        ErrorRecordResponse errorRecordResponse = new ErrorRecordResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorRecordResponse);
    }

}
