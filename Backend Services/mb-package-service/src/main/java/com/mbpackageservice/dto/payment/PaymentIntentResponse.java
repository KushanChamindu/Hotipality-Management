package com.mbpackageservice.dto.payment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentResponse {
    private String paymentIntentId;
    private String status;
    private double amount;
    private String currency;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime statusUpdatedDate;
    private LocalDateTime expireDate;
}
