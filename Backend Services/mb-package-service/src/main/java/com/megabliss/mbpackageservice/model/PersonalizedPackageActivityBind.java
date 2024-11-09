package com.megabliss.mbpackageservice.model;

import com.megabliss.mbpackageservice.model.activity.Activity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personalized_package_activity_bind")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPackageActivityBind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // this id can be used to get activity time
    private long activityTimeId;
    private int dayNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "id")
    private Activity activity;
}
