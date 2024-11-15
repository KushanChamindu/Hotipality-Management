package com.mbpackageservice.exception;

public class GeneralException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
