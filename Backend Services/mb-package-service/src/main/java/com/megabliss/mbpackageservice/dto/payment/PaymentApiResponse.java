package com.megabliss.mbpackageservice.dto.payment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
}