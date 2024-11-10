package com.mbpackageservice.service;

import org.springframework.data.domain.Pageable;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.activity.ActivityDTO;
import com.mbpackageservice.dto.activity.ActivityReviewDTO;
import com.mbpackageservice.dto.activity.ActivityUpdateDTO;
import com.mbpackageservice.model.activity.ActivityReview;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;

public interface ActivityService {
    public ActivityDTO findActivityById(Long activityId);

    public ResponseModel<ActivityDTO> findActivityByServiceProviderId(String serviceProviderId, Pageable pageable);

    public ActivityDTO addActivity(ActivityDTO activityDTO);

    public ResponseModel<ActivityDTO> findAllActivityByTitle(String title, String location, Pageable pageable);

    public ActivityReviewDTO addActivityReview(Long activityId, ActivityReviewDTO activityReview);

    public ResponseModel<ActivityReview> findActivityReviews(Long activityId, Pageable pageable);

    public ActivityDTO updateActivity(Long activityId, ActivityUpdateDTO activityUpdateDTO);

    ResponseModel<ActivityDTO> getAllActivities(Boolean isActive, Pageable pageable);

    public void deleteActivity(Long activityId);

    public ResponseModel<ActivityDTO> filteredActivities(Double latitude, Double longitude,
            Double distance, EventType type,
            EventLevel level, Float review, Boolean availability, Double lowerPrice,
            Double upperPrice, Pageable pageable);

}
