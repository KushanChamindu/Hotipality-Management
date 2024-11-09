package com.megabliss.mbpackageservice.model.activitypackage;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_package_review")
@Data @NoArgsConstructor @AllArgsConstructor
public class ActivityPackageReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;
    private float starRating;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "activity_package_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private ActivityPackage activityPackage;
}
