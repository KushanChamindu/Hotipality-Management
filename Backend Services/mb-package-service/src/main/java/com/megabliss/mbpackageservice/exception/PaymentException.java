package com.megabliss.mbpackageservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus errorStatus;

    public PaymentException(String message, String errorCode, HttpStatus errorStatus) {
        super(message);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }
}
