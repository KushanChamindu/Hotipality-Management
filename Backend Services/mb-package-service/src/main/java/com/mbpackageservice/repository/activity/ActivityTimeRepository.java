package com.mbpackageservice.repository.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activity.ActivityTime;

@Repository
public interface ActivityTimeRepository extends JpaRepository<ActivityTime, Long> {

}