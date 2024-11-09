package com.megabliss.mbpackageservice.model.packagebusiness;

import java.util.ArrayList;
import java.util.List;

import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackage;
import com.megabliss.mbpackageservice.model.enums.BusinessSize;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "package_business")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true) // Adding unique constraint
    private String serviceProviderId;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String businessLogoURL;
    private String businessCoverImageURL;
    private String webSiteURL;

    private String email;
    private String address;
    private String city;
    private String businessName;
    private Boolean isActive;
    private String phoneNumber;
    private String location;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_level", nullable = false)
    private BusinessSize businessSize;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "package_business_id", referencedColumnName = "id")
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "package_business_id", referencedColumnName = "id")
    private List<ActivityPackage> activityPackages = new ArrayList<>();
}
