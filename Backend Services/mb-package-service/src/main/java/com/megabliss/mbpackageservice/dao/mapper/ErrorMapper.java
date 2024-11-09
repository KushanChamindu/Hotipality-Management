package com.megabliss.mbpackageservice.dao.mapper;

import com.megabliss.mbpackageservice.exception.CustomError;
import com.megabliss.mbpackageservice.exception.PaymentException;
import com.megabliss.mbpackageservice.exception.ValidationCustomError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ErrorMapper {

    public CustomError mapToCustomError(Exception error, HttpStatus httpStatus) {
        CustomError customError = new CustomError();
        customError.setError(httpStatus.name());
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());
        customError.setMessage(error.getLocalizedMessage());
        return customError;
    }

    public CustomError mapToCustomError(Exception error, HttpStatus httpStatus,
            String errorCode) {
        CustomError customError = new CustomError();
        customError.setError(errorCode);
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());
        customError.setMessage(error.getLocalizedMessage());

        return customError;
    }

    public ValidationCustomError mapToValidationCustomError(MethodArgumentNotValidException error,
            HttpStatus httpStatus) {
        ValidationCustomError customError = new ValidationCustomError();
        customError.setError(httpStatus.name());
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());

        // map the list of errors here
        List<String> errorsMapped = new ArrayList<>();
        error.getBindingResult().getAllErrors().forEach((errorMsg) -> {
            String fieldName = ((FieldError) errorMsg).getField();
            String errorMessage = errorMsg.getDefaultMessage();
            errorsMapped.add(fieldName + " : " + errorMessage);
        });

        customError.setErrorMessages(errorsMapped);
        return customError;
    }

    public ValidationCustomError mapToValidationCustomError(ConstraintViolationException error, HttpStatus httpStatus) {
        ValidationCustomError customError = new ValidationCustomError();
        customError.setError(httpStatus.name());
        customError.setStatus(httpStatus.value());
        customError.setTimestamp(System.currentTimeMillis());

        List<String> errors = error.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        customError.setErrorMessages(errors);

        return customError;
    }
}