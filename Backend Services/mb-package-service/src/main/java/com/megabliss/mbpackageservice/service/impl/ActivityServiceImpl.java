package com.megabliss.mbpackageservice.service.impl;

import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.activity.ActivityDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityReviewDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityUpdateDTO;
import com.megabliss.mbpackageservice.exception.ResourceNotFoundException;
import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activity.ActivityReview;
import com.megabliss.mbpackageservice.model.enums.EventLevel;
import com.megabliss.mbpackageservice.model.enums.EventType;
import com.megabliss.mbpackageservice.model.packagebusiness.PackageBusiness;
import com.megabliss.mbpackageservice.repository.PackageActivityBindRepository;
import com.megabliss.mbpackageservice.repository.PersonalizedPackageActivityBindRepository;
import com.megabliss.mbpackageservice.repository.activity.ActivityRepository;
import com.megabliss.mbpackageservice.repository.activity.ActivityReviewRepository;
import com.megabliss.mbpackageservice.repository.packagebusiness.PackageBusinessRepository;
import com.megabliss.mbpackageservice.repository.specifications.ActivitySpecifications;
import com.megabliss.mbpackageservice.service.ActivityService;
import com.megabliss.mbpackageservice.utils.ActivityUtils;
import com.megabliss.mbpackageservice.utils.CommonPageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityReviewRepository activityReviewRepository;

    @Autowired
    private PackageActivityBindRepository activityBindRepository;

    @Autowired
    private PersonalizedPackageActivityBindRepository personalizedPackageActivityBindRepository;

    @Autowired
    private PackageBusinessRepository packageBusinessRepository;

    @Autowired
    private ActivityUtils activityUtils;

    @Autowired
    private CommonPageUtils commonPageUtils;

    @Override
    public ActivityDTO findActivityById(Long activityId) {
        Optional<Activity> activityDbResponse = activityRepository.findById(activityId);
        if(activityDbResponse.isPresent()){
            Activity activity = activityDbResponse.get();
            return activityUtils.getActivityMapper().mapToActivityDto(activity);
        }else{
            throw new ResourceNotFoundException("Record not found activity with id : " + activityId);
        }
    }
    @Override
    public ResponseModel<ActivityDTO> findActivityByServiceProviderId(String serviceProviderId, Pageable pageable) {
        Page<Activity> activities = activityRepository.findByServiceProviderId(serviceProviderId, pageable);
        return activityUtils.buildActivityDTOResponse(activities);
    }

    @Override
    public ResponseModel<ActivityDTO> filteredActivities(Double latitude, Double longitude,
                                                         Double distance, EventType type,
                                                         EventLevel level, Float review, Boolean availability,
                                                         Double lowerPrice, Double upperPrice, Pageable pageable) {
        Specification<Activity> spec = ActivitySpecifications.filter(latitude, longitude,
                distance, type, level, review, availability, lowerPrice, upperPrice);
        Page<Activity> pagedActivities = activityRepository.findAll(spec, pageable);

        return activityUtils.buildActivityDTOResponse(pagedActivities);
    }


    @Override
    public ActivityDTO addActivity(ActivityDTO activityDTO) {
        Activity activity = activityUtils.getActivityMapper().mapToActivity(activityDTO);
        PackageBusiness packageBusiness = packageBusinessRepository
                .findByServiceProviderId(activityDTO.getServiceProviderId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Couldn't find business info for: " + activityDTO.getServiceProviderId()));
        // user can never be set activity average review
        activity.setAverageReview(null);
        packageBusiness.getActivities().add(activity);
        Activity activitySaved = this.activityRepository.save(activity);
        return activityUtils.getActivityMapper().mapToActivityDto(activitySaved);
    }

    @Override
    public ResponseModel<ActivityDTO> findAllActivityByTitle(String title, String location, Pageable pageable) {
        Page<Activity> activities = location != null
                ? activityRepository.searchByTitleContainingAndLocation(title, location, pageable)
                : activityRepository.findByTitleContaining(title, pageable);
        return activityUtils.buildActivityDTOResponse(activities);
    }

    @Override
    public ActivityReviewDTO addActivityReview(Long activityId, ActivityReviewDTO activityReviewDTO) {
        ActivityReview activityReview = activityUtils.getActivityMapper().mapToActivityReview(activityReviewDTO);
        Optional<Activity> activityDbResponse = activityRepository.findById(activityId);
        if (activityDbResponse.isPresent()) {
            Activity activity = activityDbResponse.get();
            List<Float> startRatings = activityReviewRepository.findAllStarRatingByActivityId(activityId);
            activity.setAverageReview(
                    activityUtils.calculateAverageRaview(startRatings, activityReviewDTO.getStarRating()));
            activityRepository.save(activity);
            activityReview.setActivity(activity);
            activityReview.setDateOfReview(LocalDate.now());
            activityReviewRepository.save(activityReview);
        } else {
            throw new ResourceNotFoundException("Record not found activity with id : " + activityId);
        }
        return activityUtils.getActivityMapper().mapToActivityReviewDTO(activityReview);
    }

    @Override
    public ResponseModel<ActivityReview> findActivityReviews(Long activityId, Pageable pageable) {
        Page<ActivityReview> activityReviews = activityReviewRepository.findByActivityId(activityId, pageable);
        return commonPageUtils.buildPagedResponseModel(activityReviews);
    }

    @Override
    public ActivityDTO updateActivity(Long activityId, ActivityUpdateDTO activityUpdateDTO) {
        Optional<Activity> activityDbResponse = activityRepository.findById(activityId);
        if (activityDbResponse.isPresent()) {
            Activity activity = activityDbResponse.get();
            Activity update = activityUtils.getActivityMapper().updateActivity(activityUpdateDTO, activity);
            Activity updateAndSaved = activityRepository.save(update);
            return activityUtils.getActivityMapper().mapToActivityDto(updateAndSaved);
        } else {
            throw new ResourceNotFoundException("Record not found activity with id : " + activityId);
        }
    }

    @Override
    public ResponseModel<ActivityDTO> getAllActivities(Boolean isActive, Pageable pageable) {
        Page<Activity> activities = activityRepository.findByIsActive(isActive, pageable);
        return activityUtils.buildActivityDTOResponse(activities);
    }

    @Override
    public void deleteActivity(Long activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);

        if (activity.isPresent()) {
            activityBindRepository.deleteByActivity_Id(activityId);
            personalizedPackageActivityBindRepository.deleteByActivity_Id(activityId);
            activityReviewRepository.deleteByActivity_Id(activityId);
            activityRepository.deleteById(activityId);

        } else {
            throw new ResourceNotFoundException("Record not found activity with id : " + activityId);
        }

    }
}
