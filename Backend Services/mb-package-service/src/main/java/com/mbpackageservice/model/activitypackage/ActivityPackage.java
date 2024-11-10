package com.mbpackageservice.model.activitypackage;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.mbpackageservice.model.PackageActivityBind;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;

@Entity
@Table(name = "activity_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceProviderId;

    private String timeRequired;
    private String email;
    private String address;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String about;

    private int minHeadCount;
    private int maxHeadCount;
    private String title;
    private Boolean isActive;
    private String phoneNumber;
    private String age;
    private Float price;
    private Boolean isAlldayActivity;
    private Double longitude;
    private Double latitude;

    private Float averageReview;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_level", nullable = false)
    private EventLevel eventLevel;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_package_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityPackageImage> activityPackageImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_package_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityPackageVideo> activityPackageVideos = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_package_id", referencedColumnName = "id")
    private List<PackageActivityBind> packageActivityBinds = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_package_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityPackageRequirement> activityPackageRequirements = new ArrayList<>();

    public void setActivityPackageImages(List<ActivityPackageImage> activityPackageImages) {
        this.activityPackageImages.clear();
        this.activityPackageImages.addAll(activityPackageImages);
    }

    public void setActivityVideos(List<ActivityPackageVideo> activityPackageVideos) {
        this.activityPackageVideos.clear();
        this.activityPackageVideos.addAll(activityPackageVideos);
    }

    public void setActivityPackageRequirements(List<ActivityPackageRequirement> activityPackageRequirements) {
        this.activityPackageRequirements.clear();
        this.activityPackageRequirements.addAll(activityPackageRequirements);
    }
}
