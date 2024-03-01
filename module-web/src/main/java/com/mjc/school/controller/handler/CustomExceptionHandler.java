package com.mjc.school.controller.handler;

import com.mjc.school.service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, Locale locale) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getLocalizedMessage(locale))
                .build());
    }

    @ExceptionHandler(value = ResourceConflictServiceException.class)
    protected ResponseEntity<ErrorResponse> handleResourceConflictException(ServiceException e, Locale locale) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getLocalizedMessage(locale))
                .build());
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ErrorResponse> handleValidatorException(ValidatorException e, Locale locale) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getLocalizedMessage(locale))
                .build());
    }

    @ExceptionHandler(SortException.class)
    public ResponseEntity<ErrorResponse> handleSortException(SortException e, Locale locale) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getLocalizedMessage(locale))
                .build());
    }

    @ExceptionHandler(FilterException.class)
    public ResponseEntity<ErrorResponse> handleFilterException(FilterException e, Locale locale) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getLocalizedMessage(locale))
                .build());
    }
}
