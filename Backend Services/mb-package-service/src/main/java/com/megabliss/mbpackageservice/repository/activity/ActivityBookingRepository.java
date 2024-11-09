package com.megabliss.mbpackageservice.repository.activity;

import com.megabliss.mbpackageservice.model.activity.ActivityBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityBookingRepository extends JpaRepository<ActivityBooking, Long> {


}