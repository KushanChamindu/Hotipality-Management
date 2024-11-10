package com.mbpackageservice.model.activity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_special_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivitySpecialTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
