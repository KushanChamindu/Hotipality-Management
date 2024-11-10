package com.mbpackageservice.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class BookingException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final HttpStatus errorStatus;

    public BookingException(String message) {
        super(message);
        this.errorCode = HttpStatus.BAD_REQUEST.name();
        this.errorStatus = HttpStatus.BAD_REQUEST;
    }

    public BookingException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = HttpStatus.BAD_REQUEST.name();
        this.errorStatus = HttpStatus.BAD_REQUEST;
    }

    public BookingException(String message, String errorCode, HttpStatus errorStatus) {
        super(message);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }
}