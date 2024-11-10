package com.mbpackageservice.model.activity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceProviderId;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String timeRequired;
    private String email;
    private String address;
    private String title;
    private Boolean isActive;
    private String phoneNumber;
    private String age;
    private String location;
    private Double longitude;
    private Double latitude;
    private Float price;
    private Boolean isAlldayActivity;
    private int minHeadCount;
    private int maxHeadCount;
    private String description;

    private Boolean isOneTimeEvent;

    private Float averageReview;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_level", nullable = false)
    private EventLevel eventLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityImage> activityImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityVideo> activityVideos = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityTime> activityTimes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivitySpecialTime> specialActivityTimes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private List<ActivityRequirement> activityRequirements = new ArrayList<>();

    public void setSpecialActivityTimes(List<ActivitySpecialTime> specialActivityTimes) {
        this.specialActivityTimes.clear();
        this.specialActivityTimes.addAll(specialActivityTimes);
    }

    public void setActivityImages(List<ActivityImage> activityImages) {
        this.activityImages.clear();
        this.activityImages.addAll(activityImages);
    }

    public void setActivityVideos(List<ActivityVideo> activityVideos) {
        this.activityVideos.clear();
        this.activityVideos.addAll(activityVideos);
    }

    public void setActivityTimes(List<ActivityTime> activityTimes) {
        this.activityTimes.clear();
        this.activityTimes.addAll(activityTimes);
    }

    public void setActivityRequirements(List<ActivityRequirement> activityRequirements) {
        this.activityRequirements.clear();
        this.activityRequirements.addAll(activityRequirements);
    }
}
