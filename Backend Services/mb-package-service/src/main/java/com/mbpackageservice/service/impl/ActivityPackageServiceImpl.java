package com.mbpackageservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.activitypackage.ActivityPackageBindDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageReviewDTO;
import com.mbpackageservice.dto.activitypackage.ActivityPackageUpdateDTO;
import com.mbpackageservice.exception.ResourceNotFoundException;
import com.mbpackageservice.model.PackageActivityBind;
import com.mbpackageservice.model.activity.Activity;
import com.mbpackageservice.model.activity.ActivityTime;
import com.mbpackageservice.model.activitypackage.ActivityPackage;
import com.mbpackageservice.model.activitypackage.ActivityPackageReview;
import com.mbpackageservice.model.enums.EventLevel;
import com.mbpackageservice.model.enums.EventType;
import com.mbpackageservice.model.packagebusiness.PackageBusiness;
import com.mbpackageservice.repository.activity.ActivityRepository;
import com.mbpackageservice.repository.activitypackage.ActivityPackageRepository;
import com.mbpackageservice.repository.activitypackage.ActivityPackageReviewRepository;
import com.mbpackageservice.repository.packagebusiness.PackageBusinessRepository;
import com.mbpackageservice.repository.specifications.ActivityPackageSpecifications;
import com.mbpackageservice.service.ActivityPackageService;
import com.mbpackageservice.utils.ActivityPackageUtils;
import com.mbpackageservice.utils.CommonPageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityPackageServiceImpl implements ActivityPackageService {

    @Autowired
    private ActivityPackageRepository activityPackageRepository;

    @Autowired
    private ActivityPackageReviewRepository activityPackageReviewRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PackageBusinessRepository packageBusinessRepository;

    @Autowired
    private ActivityPackageUtils activityPackageUtils;

    @Autowired
    private CommonPageUtils commonPageUtils;

    public ResponseModel<ActivityPackageDTO> filteredActivityPackages(Double latitude, Double longitude,
            Double distance, EventType type,
            EventLevel level, Float review,
            Boolean availability, Double lowerPrice,
            Double upperPrice,
            Pageable pageable) {
        Specification<ActivityPackage> spec = ActivityPackageSpecifications
                .filter(latitude, longitude, distance, type, level, review, availability, lowerPrice, upperPrice);
        Page<ActivityPackage> pagedActivities = activityPackageRepository.findAll(spec, pageable);

        return activityPackageUtils.buildActivityPackageDTOResponse(pagedActivities);
    }

    @Override
    public ActivityPackageDTO addActivityPackages(ActivityPackageDTO activityPackageDTO) {
        ActivityPackage activityPackage = activityPackageUtils.getActivityPackageMapper()
                .mapToActivityPackage(activityPackageDTO);
        PackageBusiness packageBusiness = packageBusinessRepository
                .findByServiceProviderId(activityPackageDTO.getServiceProviderId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Couldn't find business info for: " + activityPackageDTO.getServiceProviderId()));
        // user can never be set activity package average review, since it is calculated
        // by system
        activityPackage.setAverageReview(null);
        packageBusiness.getActivityPackages().add(activityPackage);
        ActivityPackage activityPackageSaved = this.activityPackageRepository.save(activityPackage);
        return activityPackageUtils.getActivityPackageMapper().mapToActivityPackageDto(activityPackageSaved);
    }

    @Override
    public ResponseModel<ActivityPackageDTO> findAllActivityPackagesByTitle(String title, Pageable pageable) {
        Page<ActivityPackage> activityPackages = activityPackageRepository.findByTitleContaining(title, pageable);
        return activityPackageUtils.buildActivityPackageDTOResponse(activityPackages);
    }

    @Override
    public ActivityPackageDTO findActivityPackageByActivityPackageId(Long activityPackageId) {
        Optional<ActivityPackage> activityPackageDbResponse = activityPackageRepository.findById(activityPackageId);
        if (activityPackageDbResponse.isPresent()) {
            ActivityPackage activityPackage = activityPackageDbResponse.get();
            return activityPackageUtils.getActivityPackageMapper().mapToActivityPackageDto(activityPackage);
        } else {
            throw new ResourceNotFoundException("Record not found activity package with id : " + activityPackageId);
        }
    }

    @Override
    public ActivityPackageDTO updateActivityPackage(Long activityPackageId,
            ActivityPackageUpdateDTO activityPackageUpdateDTO) {
        Optional<ActivityPackage> activityPackageDbResponse = activityPackageRepository.findById(activityPackageId);
        if (activityPackageDbResponse.isPresent()) {
            ActivityPackage activityPackage = activityPackageDbResponse.get();
            ActivityPackage updateActivityPackage = activityPackageUtils.getActivityPackageMapper()
                    .updateActivityPackage(activityPackageUpdateDTO, activityPackage);
            ActivityPackage updateAndSaved = activityPackageRepository.save(updateActivityPackage);
            return activityPackageUtils.getActivityPackageMapper().mapToActivityPackageDto(updateAndSaved);
        } else {
            throw new ResourceNotFoundException("Record not found activity package with id : " + activityPackageId);
        }
    }

    @Override
    public ActivityPackageReviewDTO addActivityPackageReview(Long activityPackageId,
            ActivityPackageReviewDTO activityPackageReviewDTO) {
        ActivityPackageReview activityPackageReview = activityPackageUtils.getActivityPackageMapper()
                .mapToActivityPackageReview(activityPackageReviewDTO);
        Optional<ActivityPackage> activityPackageDbResponse = activityPackageRepository.findById(activityPackageId);
        if (activityPackageDbResponse.isPresent()) {
            ActivityPackage activityPackage = activityPackageDbResponse.get();
            List<Float> startRatings = activityPackageReviewRepository.findAllStarRatingByActivityId(activityPackageId);
            activityPackage.setAverageReview(activityPackageUtils.calculateAverageRaview(startRatings,
                    activityPackageReviewDTO.getStarRating()));
            activityPackageRepository.save(activityPackage);
            activityPackageReview.setActivityPackage(activityPackage);
            activityPackageReviewRepository.save(activityPackageReview);
        } else {
            throw new ResourceNotFoundException("Record not found activity package with id : " + activityPackageId);
        }
        return activityPackageUtils.getActivityPackageMapper().mapToActivityPackageReviewDTO(activityPackageReview);
    }

    @Override
    public ResponseModel<ActivityPackageReview> findActivityPackageReviews(Long activityPackageId, Pageable pageable) {
        Page<ActivityPackageReview> activityPackageReviews = activityPackageReviewRepository
                .findByActivityPackageId(activityPackageId, pageable);
        return commonPageUtils.buildPagedResponseModel(activityPackageReviews);
    }

    @Override
    public ActivityPackageDTO addActivitiesToPackage(Long activityPackageId,
            List<ActivityPackageBindDTO> activityPackageBindDTO) {
        Optional<ActivityPackage> activityPackageDbResponse = activityPackageRepository.findById(activityPackageId);
        if (activityPackageDbResponse.isPresent()) {
            ActivityPackage activityPackage = activityPackageDbResponse.get();
            List<PackageActivityBind> packageActivityBinds = createActivityBindList(activityPackageBindDTO);
            activityPackage.getPackageActivityBinds().clear();
            activityPackage.getPackageActivityBinds().addAll(packageActivityBinds);
            ActivityPackage savedActivityPackage = activityPackageRepository.save(activityPackage);
            return activityPackageUtils.getActivityPackageMapper().mapToActivityPackageDto(savedActivityPackage);
        } else {
            throw new ResourceNotFoundException("Record not found activity package with id : " + activityPackageId);
        }
    }

    @Override
    public List<ActivityPackageDTO> findProviderActivityPackages(String providerId) {
        List<ActivityPackage> activityPackage = activityPackageRepository.findByServiceProviderId(providerId);

        return activityPackage.stream()
                .map(activityPackageUtils.getActivityPackageMapper()::mapToActivityPackageDto)
                .toList();
    }

    @Override
    public void deleteActivityPackageById(Long activityPackageId) {
        Optional<ActivityPackage> activityPackageDbResponse = activityPackageRepository.findById(activityPackageId);

        if (activityPackageDbResponse.isPresent()) {
            activityPackageReviewRepository.deleteByActivityPackage_Id(activityPackageId);
            activityPackageRepository.deleteById(activityPackageId);
        } else {
            throw new ResourceNotFoundException("Record not found activity package with id : " + activityPackageId);
        }
    }

    private List<PackageActivityBind> createActivityBindList(List<ActivityPackageBindDTO> activityPackageBindDTOs) {
        List<Long> activityIdList = activityPackageBindDTOs.stream().map(ActivityPackageBindDTO::getActivityId)
                .collect(Collectors.toList());
        List<Activity> activityList = activityRepository.findAllById(activityIdList);
        List<PackageActivityBind> packageActivityBinds = new ArrayList<>();
        for (ActivityPackageBindDTO activityPackageBindDTO : activityPackageBindDTOs) {
            PackageActivityBind packageActivityBind = new PackageActivityBind();

            // get related activity to bind with package
            Activity relatedActivity = activityList.stream()
                    .filter(activiy -> activityPackageBindDTO.getActivityId().equals(activiy.getId())).findAny()
                    .orElse(null);
            // check for activity time id
            ActivityTime relatedActivityTime = relatedActivity == null ? null
                    : relatedActivity.getActivityTimes().stream()
                            .filter(activityTime -> activityPackageBindDTO.getActivityTimeId()
                                    .equals(activityTime.getId()))
                            .findAny().orElse(null);
            if (relatedActivity != null && relatedActivityTime != null) {
                packageActivityBind.setActivity(relatedActivity);
                packageActivityBind.setActivityTimeId(activityPackageBindDTO.getActivityTimeId());
                packageActivityBind.setDayNumber(activityPackageBindDTO.getDayNumber());
                packageActivityBinds.add(packageActivityBind);
            }
        }
        return packageActivityBinds;
    }

    @Override
    public ResponseModel<ActivityPackageDTO> getMyPackageActivities(Boolean isActive, Pageable pageable) {
        Page<ActivityPackage> packages;
        if (isActive != null) {
            packages = activityPackageRepository.findByIsActive(isActive, pageable);
        } else {
            packages = activityPackageRepository.findAll(pageable);
        }
        return activityPackageUtils.buildActivityPackageDTOResponse(packages);
    }
}
