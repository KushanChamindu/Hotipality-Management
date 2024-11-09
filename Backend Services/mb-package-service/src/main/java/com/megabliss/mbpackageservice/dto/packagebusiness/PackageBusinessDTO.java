package com.megabliss.mbpackageservice.dto.packagebusiness;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackage;
import com.megabliss.mbpackageservice.model.enums.BusinessSize;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageBusinessDTO {

    private Long packageBusinessId;

    @NotNull(message = "Invalid Id : Service Provider Id cannot be null")
    @NotBlank(message = "Invalid Id : Service Provider Id cannot be blank")
    private String serviceProviderId;

    @NotNull(message = "Invalid About : PackageBusiness About section cannot be null")
    @NotBlank(message = "Invalid About : PackageBusiness About section cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid About Length : PackageBusiness About must be between 3-255 characters")
    private String description;

    @Size(min = 3, max = 128, message = "Invalid Time Required Length : PackageBusiness businessLogoURL Required must be between 3-128 characters")
    private String businessLogoURL;

    @Size(min = 3, max = 128, message = "Invalid Time Required Length : PackageBusiness businessCoverImageURL Required must be between 3-128 characters")
    private String businessCoverImageURL;

    @Size(min = 3, max = 128, message = "Invalid Time Required Length : PackageBusiness webSiteURL Required must be between 3-128 characters")
    private String webSiteURL;

    @Email(message = "Invalid email")
    @NotNull(message = "Invalid Email : PackageBusiness Email cannot be null")
    @NotBlank(message = "Invalid Email : PackageBusiness Email cannot be blank")
    private String email;

    @NotNull(message = "Invalid Address : PackageBusiness Address cannot be null")
    @NotBlank(message = "Invalid Address : PackageBusiness Address cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid Address Length : PackageBusiness Address must be between 3-255 characters")
    private String address;

    @NotNull(message = "Invalid Address : PackageBusiness city cannot be null")
    @NotBlank(message = "Invalid Address : PackageBusiness city cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid city Length : PackageBusiness city must be between 3-255 characters")
    private String city;

    @NotNull(message = "Invalid Title : businessName cannot be null")
    @NotBlank(message = "Invalid Title : businessName cannot be blank")
    @Size(min = 3, max = 128, message = "Invalid businessName Length : AbusinessNamee must be between 3-128 characters")
    private String businessName;

    @NotNull(message = "Invalid Active Status : isActive Status cannot be null")
    private Boolean isActive;

    @NotNull(message = "Invalid Phone Number : PackageBusiness Phone Number cannot be null")
    @NotBlank(message = "Invalid Phone Number : PackageBusiness Phone Number cannot be blank")
    @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Invalid Phone Number : Must be 3-12 digits long with digits 0-9")
    private String phoneNumber;

    @NotNull(message = "Invalid Location : PackageBusiness Location cannot be null")
    @NotBlank(message = "Invalid Location : PackageBusiness Location cannot be blank")
    @Size(min = 3, max = 255, message = "Invalid Location Length : PackageBusiness Location must be between 3-255 " +
            "characters")
    private String location;

    private Double latitude;
    private Double longitude;

    private BusinessSize businessSize;

    private List<Activity> activities;
    private List<ActivityPackage> activityPackages;
}
