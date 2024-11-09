package com.megabliss.mbpackageservice.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.megabliss.mbpackageservice.model.payment.PaymentInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBookingDTO {

    @NotBlank(message = "Invalid Customer Id : Activity Booking Customer Id must not be blank or null")
    private String customerId;

    @FutureOrPresent(message = "Invalid From Date : Date cannot be before current date")
    private LocalDate fromDate;

    @NotBlank(message = "Invalid User Name : Must not be blank or null")
    private String userName;
    @NotBlank(message = "Invalid Phone Number : Must not be blank or null")
    @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Invalid Phone Number : Must be 3-12 digits long with digits 0-9")
    private String phoneNumber;
    private String notes;

    @FutureOrPresent(message = "Invalid To Date : Date cannot be before current date")
    private LocalDate toDate;

    @NotNull(message = "Invalid boolean check : isOneTime must not be null")
    private Boolean isOneTime;

    private Long activityTimeId;
    private Long specialActivityTimeId;

    @NotNull(message = "Invalid Quantity : Must not be null")
    @PositiveOrZero(message = "Invalid Quantity Size : Must be positive or zero value integer")
    private int quantity;

    @NotNull(message = "Invalid Activity Id : Must not be null")
    private Long activityId;

    private PaymentInfo paymentInfo;
}
