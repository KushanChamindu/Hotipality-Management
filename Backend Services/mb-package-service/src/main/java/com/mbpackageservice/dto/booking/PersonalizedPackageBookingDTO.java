package com.mbpackageservice.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.mbpackageservice.model.activity.ActivityBooking;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPackageBookingDTO {

    @NotNull(message = "Invalid Customer Id : Must not be null")
    private String customerId;

    @NotNull(message = "Invalid Booking Date : Must not be null")
    @FutureOrPresent(message = "Invalid From Date : Date cannot be before current date")
    private LocalDate fromDate;
    @NotNull(message = "Invalid Booking Date : Must not be null")
    @FutureOrPresent(message = "Invalid To Date : Date cannot be before current date")
    private LocalDate toDate;

    @NotBlank(message = "Invalid User Name : Must not be blank or null")
    private String userName;
    @NotBlank(message = "Invalid Phone Number : Must not be blank or null")
    @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Invalid Phone Number : Must be 3-12 digits long with digits 0-9")
    private String phoneNumber;
    private String notes;
    @NotNull(message = "Invalid Quantity : Must not be null")
    @PositiveOrZero(message = "Invalid Quantity : Must be positive or zero value integer")
    private int quantity;

    @NotNull(message = "Invalid Personalized Package Id : Must not be null")
    private Long personalizedPackageId;

    private List<ActivityBooking> activityBookings;
}
