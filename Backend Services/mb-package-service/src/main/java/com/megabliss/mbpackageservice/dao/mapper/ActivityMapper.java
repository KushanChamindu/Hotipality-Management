package com.megabliss.mbpackageservice.dao.mapper;

import com.megabliss.mbpackageservice.dto.activity.ActivityDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityReviewDTO;
import com.megabliss.mbpackageservice.dto.activity.ActivityUpdateDTO;
import com.megabliss.mbpackageservice.model.activity.Activity;
import com.megabliss.mbpackageservice.model.activity.ActivityReview;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ActivityMapper {

    @Autowired
    private ModelMapper modelMapper;

    // convert activity Jpa Entity into ActivityDTO
    public ActivityDTO mapToActivityDto(Activity activity) {
        return modelMapper.map(activity, ActivityDTO.class);
    }

    // Convert ActivityDTO to Activity JPA Entity
    public Activity mapToActivity(ActivityDTO activityDTO) {
        return modelMapper.map(activityDTO, Activity.class);
    }

    public Activity updateActivity(ActivityUpdateDTO updatedActivity, Activity activity) {
        Long activityId = activity.getId();
        modelMapper.map(updatedActivity, activity);
        activity.setId(activityId);
        return activity;
    }

    public ActivityReviewDTO mapToActivityReviewDTO(ActivityReview activityReview) {
        return modelMapper.map(activityReview,
                ActivityReviewDTO.class);
    }

    public ActivityReview mapToActivityReview(ActivityReviewDTO activityReviewDTO) {
        return modelMapper.map(activityReviewDTO,
                ActivityReview.class);
    }
}