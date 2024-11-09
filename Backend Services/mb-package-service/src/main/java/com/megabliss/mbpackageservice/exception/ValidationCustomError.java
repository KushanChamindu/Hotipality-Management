package com.megabliss.mbpackageservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationCustomError {
    private int status;
    private String error;
    private List<String> errorMessages; //for validation errors
    private long timestamp;
}
