package com.megabliss.mbpackageservice.dto.activitypackage;

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
public class ActivityPackageBindDTO {

    private long id;

    @NotNull(message = "Invalid Activity Time Id : Must provide valid activity time id")
    // this id can be used to get activity time
    private Long activityTimeId;

    @NotNull(message = "Invalid Day Number : Must provide non null day number")
    @PositiveOrZero(message = "Invalid Day Number : Must provide positive integer or zero value")
    private int dayNumber;

    @NotNull(message = "Invalid Activity Id : Must provide activity id that is valid")
    //to get Activity id
    private Long activityId;
}
