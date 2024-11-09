package com.megabliss.mbpackageservice.service.impl;

import com.megabliss.mbpackageservice.dto.payment.CreatePaymentIntentRequest;
import com.megabliss.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.megabliss.mbpackageservice.dto.payment.PaymentApiResponse;
import com.megabliss.mbpackageservice.exception.PaymentException;
import com.megabliss.mbpackageservice.service.PaymentService;
import com.megabliss.mbpackageservice.utils.externalapi.HttpClient;
import com.megabliss.mbpackageservice.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private HttpClient httpClient;

    @Value("${payment_service.url}")
    private String paymentURLPrefix;

    @Value("${payment_service.batch_size}")
    private int paymentBatchSize;

    @Override
    public PaymentIntentResponse createPayementIntent(String userId, String serviceProviderId, double amount,
            String description, String currency) {
        if (!"nzd".equals(currency)) {
            throw new PaymentException("Currency is should be NZD!!", "CURRENCY_ERROR", HttpStatus.BAD_REQUEST);
        }

        CreatePaymentIntentRequest createIntentRequest = CreatePaymentIntentRequest.builder().userId(userId)
                .serviceProviderId(serviceProviderId).amount(amount).description(description).currency(currency)
                .build();
        String paymentUrl = paymentURLPrefix + "/single-payment/service/create-intent";
        ResponseEntity<PaymentApiResponse<PaymentIntentResponse>> response;
        try {
            response = httpClient.post(paymentUrl,
                    createIntentRequest,
                    new ParameterizedTypeReference<PaymentApiResponse<PaymentIntentResponse>>() {
                    });
        } catch (Exception e) {
            throw new PaymentException("Couldn't get payment. Payment service unavaible!", "PAYMENT_INFO_ERROR",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                || !response.getBody().isSuccess()) {
            throw new PaymentException("Couldn't get payment info for this request!", "PAYMENT_INFO_ERROR",
                    HttpStatus.resolve(response.getStatusCode().value()));
        }
        return response.getBody().getData();
    }

    @Override
    public List<PaymentIntentResponse> getPaymentIntents(String userId, List<String> paymentIntentIds) {
        List<PaymentIntentResponse> finalResponse = new ArrayList<>();
        List<List<String>> resizedBatchList = Tools.chunkArrayList(paymentIntentIds, paymentBatchSize);
        for (int i = 0; i < resizedBatchList.size(); i++) {
            String suffixURL = "/single-payment/payment-details/user/" + userId + "/payment-intents/"
                    + String.join(",", resizedBatchList.get(i));
            String paymentUrl = paymentURLPrefix + suffixURL;
            ResponseEntity<PaymentApiResponse<List<PaymentIntentResponse>>> response;
            try {
                response = httpClient.get(paymentUrl,
                        new ParameterizedTypeReference<PaymentApiResponse<List<PaymentIntentResponse>>>() {
                        });
            } catch (Exception e) {
                throw new PaymentException("Couldn't get payment. Payment service unavaible!", "PAYMENT_INFO_ERROR",
                        HttpStatus.SERVICE_UNAVAILABLE);
            }
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || !response.getBody().isSuccess()) {
                throw new PaymentException("Couldn't get payment info for this request!", "PAYMENT_INFO_ERROR",
                        HttpStatus.resolve(response.getStatusCode().value()));
            }
            finalResponse.addAll(response.getBody().getData());
        }
        return finalResponse;
    }
}