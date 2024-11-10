package com.mbpackageservice.dao.mapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.mbpackageservice.model.payment.PaymentInfo;

@Component
@Getter
public class PaymentDataMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Convert CreatePaymentIntentResponse to PaymentInfo JPA Entity
    public PaymentInfo mapToPaymentInfo(PaymentIntentResponse paymentIntentResponse) {
        return modelMapper.map(paymentIntentResponse, PaymentInfo.class);
    }

}