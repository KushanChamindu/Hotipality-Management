package com.mbpackageservice.dto.activitypackage;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.mbpackageservice.model.PackageActivityBind;
import com.mbpackageservice.model.activitypackage.ActivityPackageImage;
import com.mbpackageservice.model.activitypackage.ActivityPackageRequirement;
import com.mbpackageservice.model.activitypackage.ActivityPackageVideo;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackageDTO {

        private long activityPackageId;

        @NotNull(message = "Invalid Id : Service Provider Id cannot be null")
        @NotBlank(message = "Invalid Id : Service Provider Id cannot be blank")
        private String serviceProviderId;

        @NotNull(message = "Invalid Title : Activity Package Title cannot be null")
        @NotBlank(message = "Invalid Title : Activity Package Title cannot be blank")
        @Size(min = 3, max = 128, message = "Invalid Title Length : Activity Package Title must be between 3-128 " +
                        "characters")
        private String title;

        @NotNull(message = "Invalid Time Required : Activity Package Time Required cannot be null")
        @NotBlank(message = "Invalid Time Required : Activity Package Time Required cannot be blank")
        @Size(min = 3, max = 128, message = "Invalid Time Required Length : Activity Package Time Required must be between 3-128 "
                        +
                        "characters")
        private String timeRequired;

        @Email(message = "Invalid email")
        @NotNull(message = "Invalid Email : Activity Package Email cannot be null")
        @NotBlank(message = "Invalid Email : Activity Package Email cannot be blank")
        private String email;

        @NotNull(message = "Invalid Address : Activity Package Address cannot be null")
        @NotBlank(message = "Invalid Address : Activity Package Address cannot be blank")
        @Size(min = 3, max = 255, message = "Invalid Address Length : Activity Package Address must be between " +
                        "3-255 characters")
        private String address;

        @NotNull(message = "Invalid description : Activity description cannot be null")
        @NotBlank(message = "Invalid description : Activity description cannot be blank")
        @Size(min = 3, max = 512, message = "Invalid description Length : Activity description must be between 3-512 characters")
        private String description;

        @NotNull(message = "Invalid About : Activity Package About section cannot be null")
        @NotBlank(message = "Invalid About : Activity Package section cannot be blank")
        @Size(min = 3, max = 255, message = "Invalid About Length : Activity Package About must be between " +
                        "3-255 characters")
        private String about;

        @PositiveOrZero(message = "Invalid Minimum Head Count : Must be positive or zero value integer")
        @NotNull(message = "Invalid Minimum Head Count : Activity Package Minimum Head Count cannot be null")
        private int minHeadCount;

        @PositiveOrZero(message = "Invalid Maximum Head Count : Must be positive or zero value integer")
        @NotNull(message = "Invalid Max Head Count : Activity Package Max Head Count cannot be null")
        private int maxHeadCount;

        @NotNull(message = "Invalid Active Status : Activity Package Active Status cannot be null")
        private Boolean isActive;

        @NotNull(message = "Invalid Phone Number : Activity Package Phone Number cannot be null")
        @NotBlank(message = "Invalid Phone Number : Activity Package Phone Number cannot be blank")
        @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Invalid Phone Number : Must be 3-12 digits long with digits 0-9")
        private String phoneNumber;

        @NotNull(message = "Invalid Age Requirement : Activity Package Age Requirement cannot be null")
        @NotBlank(message = "Invalid Age Requirement : Activity Package Age Requirement cannot be blank")
        @Size(min = 3, max = 128, message = "Invalid Age Requirement Length : Activity Package Age Requirement must be "
                        +
                        "between " +
                        "3-128 characters")
        private String age;

        @NotNull(message = "Invalid Price : Activity Package Price cannot be null")
        @PositiveOrZero(message = "Invalid Price: Price must be a positive value")
        private Float price;

        @NotNull(message = "Invalid All Day Status : Activity Package All Day Status cannot be null")
        private Boolean isAlldayActivity;

        private Double longitude;
        private Double latitude;

        // didn't add message because any user can't set this field, it is calculated by
        // system
        @DecimalMin(value = "0.0", inclusive = true)
        @DecimalMax(value = "5.0", inclusive = true)
        private Float averageReview;

        private EventType eventType;

        private EventLevel eventLevel;

        @NotNull(message = "Invalid Activity Images : Activity Package Images cannot be null")
        private List<ActivityPackageImage> activityPackageImages;
        @NotNull(message = "Invalid Activity Package Videos : Activity Package Videos cannot be null")
        private List<ActivityPackageVideo> activityPackageVideos;

        // @NotNull(message = "Invalid Activity Package Binds : Activity Package Binds
        // cannot be null")
        private List<PackageActivityBind> packageActivityBinds;

        @NotNull(message = "Invalid Activity Package Requirements : Activity Package Requirements cannot be null")
        private List<ActivityPackageRequirement> activityPackageRequirements;
}
