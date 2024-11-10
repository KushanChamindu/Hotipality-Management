package com.mbpackageservice.model.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_booking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // this id can be used to get activity time
    private Long activityTimeId;
    // if activityTime is null then the activity is a one time event.
    private Long specialActivityTimeId;
    private int quantity;

    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "id")
    private Activity activity;
}
