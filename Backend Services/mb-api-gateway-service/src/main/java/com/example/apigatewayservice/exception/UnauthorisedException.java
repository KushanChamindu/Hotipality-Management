package com.example.apigatewayservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorisedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final HttpStatus httpStatus;

    public UnauthorisedException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public UnauthorisedException(String message, String errorCode, HttpStatus httpStatus, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
