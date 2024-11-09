package com.example.megablissapigatewayservice.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import com.example.megablissapigatewayservice.dao.mapper.ErrorMapper;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorMapper errorMapper;

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<CustomError> handleResourceNotFoundException(UnauthorisedException error) {
        // Handle exceptions here
        return ResponseEntity.status(error.getHttpStatus())
                .body(errorMapper.mapToCustomError(error, error.getHttpStatus()));
    }
}
