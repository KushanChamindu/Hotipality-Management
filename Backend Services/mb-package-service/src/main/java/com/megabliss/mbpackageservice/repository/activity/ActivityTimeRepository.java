package com.megabliss.mbpackageservice.repository.activity;

import com.megabliss.mbpackageservice.model.activity.ActivityTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityTimeRepository extends JpaRepository<ActivityTime, Long> {


}