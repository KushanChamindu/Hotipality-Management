package com.megabliss.mbpackageservice.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentIntentRequest {
    private double amount;
    private String currency;
    private String description;
    private String userId;
    private String serviceProviderId;
}
