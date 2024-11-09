package com.example.megablissmainservice.DAO.mapper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.megablissmainservice.exception.CustomError;
import com.example.megablissmainservice.exception.ValidationError;

@Component
public class ErrorMapper {

    public ValidationError mapTovalidationError(List<String> errorMessages, HttpStatus httpStatus) {
        ValidationError validationError = new ValidationError();
        validationError.setError(httpStatus.name());
        validationError.setStatus(httpStatus.value());
        validationError.setTimestamp(System.currentTimeMillis());
        validationError.setMessages(errorMessages);
        return validationError;
    }

    public CustomError mapToCustomError(RuntimeException ex, HttpStatus httpStatus, String error) {
        CustomError customError = new CustomError();
        customError.setError(error);
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());
        customError.setMessage(ex.getLocalizedMessage());
        return customError;
    }

    public CustomError mapToCustomError(Exception ex, HttpStatus httpStatus) {
        CustomError customError = new CustomError();
        customError.setError(ex.getMessage());
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());
        customError.setMessage(ex.getLocalizedMessage());
        return customError;
    }
}