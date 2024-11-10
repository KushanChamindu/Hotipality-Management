package com.mbpackageservice.repository.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbpackageservice.model.activity.ActivityImage;

@Repository
public interface ActivityImageRepository extends JpaRepository<ActivityImage, Long> {

}