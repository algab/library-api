package com.example.libraryapi.exceptions.handler;

import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.exceptions.ResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException ex) {
        ResponseException exception = new ResponseException(
                ex.getStatus(),
                ex.getError(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(exception);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest web
    ) {
        ResponseException exception = new ResponseException(
                status.value(),
                "BAD_REQUEST",
                "Required request body is missing",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest web
    ) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        String message = errors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        ResponseException exception = new ResponseException(
                status.value(),
                "BAD_REQUEST",
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exception, headers, status);
    }
}
