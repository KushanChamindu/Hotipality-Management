package com.megabliss.mbpackageservice.dto.activity;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReviewDTO {

    private long id;

    @NotBlank(message = "Invalid User Id : User id must not be blank or null")
    private String userId;

    @Size(min = 3, max = 255, message = "Invalid user profile URL : Must be between 3-128 characters")
    private String userProfileUrl;

    @FutureOrPresent(message = "Invalid From Date : Date cannot be before current date")
    private LocalDate dateOfReview;

    @NotNull(message = "Invalid Rating : Must not be null")
    @PositiveOrZero(message = "Invalid Rating : Rating must be a positive or zero value integer")
    @DecimalMin(value = "0.0", inclusive = true, message = "Star Rating should be grater than 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Star Rating should be less than 5.0")
    private Float starRating;

    @Size(min = 3, max = 255, message = "Invalid Review Text : Must be between 3-128 characters")
    private String reviewText;
}
