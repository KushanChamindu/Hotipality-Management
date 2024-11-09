package com.megabliss.mbpackageservice.dto.personalizedpackage;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPackageActivityBindDTO {


    private long id;

    // this id can be used to get activity time
    @NotNull(message = "Invalid Time Id : Must not be null")
    private Long activityTimeId;

    @NotNull(message = "Invalid Day Number : Must not be null")
    @PositiveOrZero(message = "Invalid Day Number : Must be a positive or zero value integer")
    private int dayNumber;

    @NotNull(message = "Invalid Activity Id : Must not be null")
    //to get Activity id
    private Long activityId;
}
