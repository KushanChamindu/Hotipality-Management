package com.example.mainservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_statistic")
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private LocalDate date;
    @Column(nullable = false)
    private int totalUsersCount;
    @Column(nullable = false)
    private int activeUsersCount;
    @Column(nullable = false)
    private int newUsersCount;
}
