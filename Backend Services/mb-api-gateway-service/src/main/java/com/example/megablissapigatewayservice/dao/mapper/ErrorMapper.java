package com.example.megablissapigatewayservice.dao.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.megablissapigatewayservice.exception.CustomError;

@Component
public class ErrorMapper {

    public CustomError mapToCustomError(RuntimeException error, HttpStatus httpStatus){
        CustomError customError = new CustomError();
        customError.setError(httpStatus.name());
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());
        customError.setMessage(error.getLocalizedMessage());
        return customError;
    }
}