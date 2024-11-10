package com.mbpackageservice.model.payment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "payment_status")
    private String status;

    @Column(name = "payment_amount")
    private double amount;

    @Column(name = "payment_currency")
    private String currency;

    @Column(name = "payment_expire_date")
    private LocalDateTime expireDate;
}
