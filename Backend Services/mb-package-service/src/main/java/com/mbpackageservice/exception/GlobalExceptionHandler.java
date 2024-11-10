package com.mbpackageservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mbpackageservice.dao.mapper.ErrorMapper;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorMapper errorMapper;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> handleResourceNotFoundException(ResourceNotFoundException e) {
        // Handle exceptions here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.mapToCustomError(e, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AuthenticatedDetailMissMatchError.class)
    public ResponseEntity<CustomError> handleAuthenticatedDetailMissMatchError(AuthenticatedDetailMissMatchError e) {
        // Handle exceptions here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.mapToCustomError(e, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<CustomError> handleBookingError(BookingException e) {
        // Handle exceptions here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.mapToCustomError(e, e.getErrorStatus(), e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationCustomError> handleInputValidationError(MethodArgumentNotValidException e) {
        // Handle exceptions here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.mapToValidationCustomError(e, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomError> handleIllegalArgumentError(IllegalArgumentException e) {
        // Handle exceptions here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.mapToCustomError(e,

                HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NullArgumentException.class)
    public ResponseEntity<CustomError> handleNullArgumentError(NullArgumentException e) {
        // handle exceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.mapToCustomError(e,
                HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationCustomError> handleUserException(ConstraintViolationException e) {
        // handle exceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.mapToValidationCustomError(e,
                HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomError> handleGeneralError(GeneralException e) {
        // handle exceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.mapToCustomError(e,
                HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<CustomError> handleAdvertisementException(PaymentException exception) {
        return ResponseEntity.status(exception.getErrorStatus())
                .body(errorMapper.mapToCustomError(exception, exception.getErrorStatus(), exception.getErrorCode()));
    }

    /**
     * Catch any other exception not specified
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMapper.mapToCustomError(e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}