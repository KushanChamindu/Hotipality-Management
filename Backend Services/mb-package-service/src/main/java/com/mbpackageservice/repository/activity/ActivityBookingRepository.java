package com.mbpackageservice.repository.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activity.ActivityBooking;

@Repository
public interface ActivityBookingRepository extends JpaRepository<ActivityBooking, Long> {

}