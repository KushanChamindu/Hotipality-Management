package com.megabliss.mbpackageservice.dao.mapper;

import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageReviewDTO;
import com.megabliss.mbpackageservice.dto.activitypackage.ActivityPackageUpdateDTO;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackage;
import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackageReview;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ActivityPackagMapper {
    @Autowired
    private ModelMapper modelMapper;

    // convert ActivityPackage Jpa Entity into ActivityPackageDTO
    public ActivityPackageDTO mapToActivityPackageDto(ActivityPackage activityPackage) {
        return modelMapper.map(activityPackage, ActivityPackageDTO.class);
    }

    // Convert ActivityPackageDTO to ActivityPackage JPA Entity
    public ActivityPackage mapToActivityPackage(ActivityPackageDTO activityPackageDTO) {
        return modelMapper.map(activityPackageDTO, ActivityPackage.class);
    }

    public ActivityPackage updateActivityPackage(ActivityPackageUpdateDTO updatedActivityPackage,
                                                 ActivityPackage activityPackage) {
        Long activityPackageId = activityPackage.getId();
        modelMapper.map(updatedActivityPackage, activityPackage);
        activityPackage.setId(activityPackageId);
        return activityPackage;
    }

    public ActivityPackageReviewDTO mapToActivityPackageReviewDTO(ActivityPackageReview activityPackageReview) {
        return modelMapper.map(activityPackageReview,
                ActivityPackageReviewDTO.class);
    }

    public ActivityPackageReview mapToActivityPackageReview(ActivityPackageReviewDTO activityPackageReviewDTO) {
        return modelMapper.map(activityPackageReviewDTO,
                ActivityPackageReview.class);
    }
}
