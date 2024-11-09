package com.example.megablissmainservice.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationError {
    private int status;
    private String error;
    private List<String> messages;
    private long timestamp;
}