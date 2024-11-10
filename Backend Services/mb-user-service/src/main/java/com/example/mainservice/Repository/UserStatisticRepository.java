package com.example.mainservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mainservice.Entity.UserStatistic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {
    List<UserStatistic> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<UserStatistic> findByDate(LocalDate date);

    boolean existsUserStatisticByDate(LocalDate date);
}
