package com.mbpackageservice.service;

import org.springframework.data.domain.Pageable;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.activitypackage.ActivityPackageBindDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageReviewDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageUpdateDTO;
import com.mbpackageservice.model.activitypackage.ActivityPackageReview;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;

import java.util.List;

public interface ActivityPackageService {

    ResponseModel<ActivityPackageDTO> filteredActivityPackages(Double latitude, Double longitude,
            Double distance, EventType type,
            EventLevel level, Float review, Boolean availability, Double lowerPrice,
            Double upperPrice, Pageable pageable);

    public ActivityPackageDTO addActivityPackages(ActivityPackageDTO activityPackageDTO);

    public ResponseModel<ActivityPackageDTO> findAllActivityPackagesByTitle(String title, Pageable pageable);

    public ActivityPackageDTO findActivityPackageByActivityPackageId(Long activityPackageId);

    public ActivityPackageDTO updateActivityPackage(Long activityId, ActivityPackageUpdateDTO activityUpdateDTO);

    public ActivityPackageReviewDTO addActivityPackageReview(Long activityPackageId,
            ActivityPackageReviewDTO activityPackageReview);

    public ResponseModel<ActivityPackageReview> findActivityPackageReviews(Long activityPackageId, Pageable pageable);

    public ActivityPackageDTO addActivitiesToPackage(Long activityPackageId,
            List<ActivityPackageBindDTO> activityPackageBindDTO);

    public ResponseModel<ActivityPackageDTO> getMyPackageActivities(Boolean isActive, Pageable pageable);

    public List<ActivityPackageDTO> findProviderActivityPackages(String providerId);

    public void deleteActivityPackageById(Long activityPackageId);
}
