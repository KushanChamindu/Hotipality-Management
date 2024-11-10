package com.example.mainservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.mainservice.DAO.mapper.ErrorMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

        @Autowired
        private ErrorMapper errorMapper;

        @ExceptionHandler(Exception.class)
        public ResponseEntity<CustomError> handleSecurityException(Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(errorMapper.mapToCustomError(exception, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationError> handleValidationErrors(MethodArgumentNotValidException ex) {
                List<String> errors = ex.getBindingResult().getFieldErrors()
                                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(errorMapper.mapTovalidationError(errors, HttpStatus.BAD_REQUEST));
        }

        @ExceptionHandler(UserServiceCustomException.class)
        public ResponseEntity<CustomError> handleUserException(UserServiceCustomException exception) {
                return ResponseEntity.status(exception.getErrorStatus())
                                .body(errorMapper.mapToCustomError(exception, exception.getErrorStatus(),
                                                exception.getErrorCode()));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ValidationError> handleUserException(ConstraintViolationException exception) {
                List<String> errors = exception.getConstraintViolations()
                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(errorMapper.mapTovalidationError(errors, HttpStatus.BAD_REQUEST));
        }

}
