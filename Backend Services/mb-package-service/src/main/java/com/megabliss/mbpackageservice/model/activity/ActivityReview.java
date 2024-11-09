package com.megabliss.mbpackageservice.model.activity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;
    private float starRating;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    private String userProfileUrl;

    private LocalDate dateOfReview;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Activity activity;
}
