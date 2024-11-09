package com.megabliss.mbpackageservice.service;

import java.util.List;

import com.megabliss.mbpackageservice.dto.payment.PaymentIntentResponse;

public interface PaymentService {
    public PaymentIntentResponse createPayementIntent(String userId, String serviceProviderId, double amount,
            String description, String currency);

    public List<PaymentIntentResponse> getPaymentIntents(String userId, List<String> paymentIntentIds);

}
