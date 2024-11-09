package com.megabliss.mbpackageservice.repository.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megabliss.mbpackageservice.model.activity.ActivityImage;

@Repository
public interface ActivityImageRepository extends JpaRepository<ActivityImage, Long>{
    
}