package com.example.megablissmainservice.utility;

import com.example.megablissmainservice.Entity.User;
import com.example.megablissmainservice.Entity.UserStatistic;
import com.example.megablissmainservice.Repository.UserRepository;
import com.example.megablissmainservice.Repository.UserStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserStatisticDailyRefreshUtil {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatisticRepository userStatisticsRepository;

//    the daily refresh will be executed at 00:00:01 am
    @Scheduled(cron = "01 00 00 * * ?")
    public void calculateUserStatistics(){

        ZoneId systemDefaultZone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(systemDefaultZone);
        Instant startOfToday = today.atStartOfDay(systemDefaultZone).toInstant();
        LocalDateTime startOfTodayLocalDateTime = today.atStartOfDay(systemDefaultZone).toLocalDateTime();
        LocalDateTime startOfThirtyDaysAgoLocalDateTime = startOfTodayLocalDateTime.minusDays(30);

        List<User> existingUserList = userRepository.findAllByDateCreationBefore(startOfToday);
        List<User> activeUserList = existingUserList.stream()
                .filter(user -> user.getLastLoginDateTime() != null && user.getLastLoginDateTime().isAfter(startOfThirtyDaysAgoLocalDateTime))
                .collect(Collectors.toList());
        List<User> newUserList = userRepository.findAllByDateCreationBetween(
                startOfToday.minus(1, ChronoUnit.DAYS),
                startOfToday.minusSeconds(1));

        Optional<UserStatistic> existingUserStatistics = userStatisticsRepository.findByDate(today.minusDays(1));
        UserStatistic userStatistics = existingUserStatistics.orElseGet(() -> {
           UserStatistic newUserStatistics = new UserStatistic();
           newUserStatistics.setDate(today.minusDays(1));
           return newUserStatistics;
        });
        userStatistics.setTotalUsersCount(existingUserList.size());
        userStatistics.setActiveUsersCount(activeUserList.size());
        userStatistics.setNewUsersCount(newUserList.size());

        userStatisticsRepository.save(userStatistics);
    }
}
