package com.megabliss.mbpackageservice.dto.personalizedpackage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPackageUpdateDTO {

    private Long personalizedPackageId;

    @NotBlank(message = "Invalid User Id : Must not be blank or null")
    private String userId;

    @NotBlank(message = "Invalid Title : Must not be blank or null")
    @Size(min = 3, max = 128, message = "Invalid Title Length : Title must be between 3-128 characters")
    private String title;
}
