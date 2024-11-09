package com.megabliss.mbpackageservice.dto.activity;

import com.megabliss.mbpackageservice.model.activity.ActivityImage;
import com.megabliss.mbpackageservice.model.activity.ActivityRequirement;
import com.megabliss.mbpackageservice.model.activity.ActivityTime;
import com.megabliss.mbpackageservice.model.activity.ActivityVideo;
import com.megabliss.mbpackageservice.model.enums.EventLevel;
import com.megabliss.mbpackageservice.model.enums.EventType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUpdateDTO {

    @NotNull(message = "Invalid Title : Activity Title cannot be null")
    @NotBlank(message = "Invalid Title : Activity Title cannot be blank")
    @Size(min = 3, max = 128, message = "Invalid Title Length : Activity Title must be between 3-128 characters")
    private String title;

    @NotNull(message = "Invalid About : Activity About section cannot be null")
    @NotBlank(message = "Invalid About : Activity About section cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid About Length : Activity About must be between 3-255 characters")
    private String about;

    @NotNull(message = "Invalid Time Required : Activity Time Required cannot be null")
    @NotBlank(message = "Invalid Time Required : Activity Time Required cannot be blank")
    @Size(min = 3, max = 128, message = "Invalid Time Required Length : Activity Time Required must be between 3-128 characters")
    private String timeRequired;

    @Email(message = "Invalid email")
    @NotNull(message = "Invalid Email : Activity Email cannot be null")
    @NotBlank(message = "Invalid Email : Activity Email cannot be blank")
    private String email;

    @NotNull(message = "Invalid Address : Activity Address cannot be null")
    @NotBlank(message = "Invalid Address : Activity Address cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid Address Length : Activity Address must be between 3-255 characters")
    private String address;

    @NotNull(message = "Invalid description : Activity Address cannot be null")
    @NotBlank(message = "Invalid description : Activity Address cannot be blank")
    @Size(min = 3, max = 512, message = "Invalid description Length : Activity Address must be between 3-512 characters")
    private String description;

    @NotNull(message = "Invalid Active Status : Activity Active Status cannot be null")
    private Boolean isActive;

    @NotNull(message = "Invalid isOneTimeEvent field : isOneTimeEvent field cannot be null")
    private Boolean isOneTimeEvent;

    @NotNull(message = "Invalid Phone Number : Activity Phone Number cannot be null")
    @NotBlank(message = "Invalid Phone Number : Activity Phone Number cannot be blank")
    @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Invalid Phone Number : Must be 3-12 digits long with digits 0-9")
    private String phoneNumber;

    @NotNull(message = "Invalid Age Requirement : Activity Age Requirement cannot be null")
    @NotBlank(message = "Invalid Age Requirement : Activity Age Requirement cannot be blank")
    @Size(min = 3, max = 128, message = "Invalid Age Requirement Length : Activity Age Requirement must be between 3-128 characters")
    private String age;
    private EventLevel eventLevel;
    private EventType eventType;

    @NotNull(message = "Invalid Location : Activity Location cannot be null")
    @NotBlank(message = "Invalid Location : Activity Location cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid Location Length : Activity Location must be between 3-255 " +
            "characters")
    private String location;
    private Double longitude;
    private Double latitude;

    @NotNull(message = "Invalid Price : Activity Price cannot be null")
    @PositiveOrZero(message = "Invalid Price: Price must be a positive value")
    private Float price;

    @NotNull(message = "Invalid All Day Status : Activity All Day Status cannot be null")
    private Boolean isAlldayActivity;

    @NotNull(message = "Invalid Minimum Head Count : Activity Minimum Head Count cannot be null")
    private int minHeadCount;

    @NotNull(message = "Invalid Max Head Count : Activity Max Head Count cannot be null")
    private int maxHeadCount;

    @NotNull(message = "Invalid Activity Images : Activity Images cannot be null")
    private List<ActivityImage> activityImages;

    @NotNull(message = "Invalid Activity Videos : Activity Videos cannot be null")
    private List<ActivityVideo> activityVideos;

    @NotNull(message = "Invalid Activity Times : Activity Activity Times cannot be null")
    private List<ActivityTime> activityTimes;

    @NotNull(message = "Invalid Activity Requirements : Activity Activity Requirements cannot be null")
    private List<ActivityRequirement> activityRequirements;
}
