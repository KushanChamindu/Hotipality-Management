package com.mbpackageservice.exception;

public class AuthenticatedDetailMissMatchError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuthenticatedDetailMissMatchError(String message) {
        super(message);
    }

    public AuthenticatedDetailMissMatchError(String message, Throwable throwable) {
        super(message, throwable);
    }
}