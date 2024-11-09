package com.megabliss.mbpackageservice.dto.activitypackage;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class ActivityPackageReviewDTO {


    private long id;

    @NotBlank(message = "Invalid User Id : Must not be blank or null")
    private String userId;

    @NotNull(message = "Invalid Star Rating : Must not be null")
    @PositiveOrZero(message = "Invalid Rating : Rating must be a positive or zero value integer")
    @DecimalMin(value = "0.0", inclusive = true, message = "Star Rating should be grater than 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Star Rating should be less than 5.0")
    private Float starRating;

    @NotBlank(message = "Invalid Review Text : Must not be blank or null")
    @Size(min = 3, max = 255, message = "Invalid Review Text Length : Must be between 3-255 characters")
    private String reviewText;
}
