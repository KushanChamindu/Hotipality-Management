package com.mbpackageservice.model.activitypackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_package_requirement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackageRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String requirementName;
    private String requirementDescription;
}
