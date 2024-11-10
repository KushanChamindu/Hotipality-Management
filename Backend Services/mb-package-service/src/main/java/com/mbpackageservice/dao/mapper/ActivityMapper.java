package com.mbpackageservice.dao.mapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dto.activity.ActivityDTO;
import com.mbpackageservice.dto.activity.ActivityReviewDTO;
import com.mbpackageservice.dto.activity.ActivityUpdateDTO;
import com.mbpackageservice.model.activity.Activity;
import com.mbpackageservice.model.activity.ActivityReview;

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