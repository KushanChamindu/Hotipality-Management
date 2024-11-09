package com.megabliss.mbpackageservice.service;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageBindDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageReviewDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageUpdateDTO;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackageReview;
import com.megabliss.mbpackageservice.model.enums.EventLevel;
import com.megabliss.mbpackageservice.model.enums.EventType;
import org.springframework.data.domain.Pageable;

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
