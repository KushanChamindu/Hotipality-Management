package com.megabliss.mbpackageservice.dao.mapper;

import com.megabliss.mbpackageservice.dto.payment.PaymentIntentResponse;
import com.megabliss.mbpackageservice.model.payment.PaymentInfo;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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