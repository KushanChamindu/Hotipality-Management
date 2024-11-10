package com.mbpackageservice.model.activity;

import java.time.LocalTime;

import com.mbpackageservice.model.enums.WeekDay;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private WeekDay weekDay;
    private int quantity;
    private LocalTime startTime;
    private LocalTime endTime;
}
