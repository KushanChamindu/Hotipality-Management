package com.megabliss.mbpackageservice.utils;


import com.megabliss.mbpackageservice.dao.mapper.ActivityMapper;
import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.activity.ActivityDTO;
import com.megabliss.mbpackageservice.model.activity.Activity;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
public class ActivityUtils {

    @Autowired
    private ActivityMapper activityMapper;

    public Float calculateAverageRaview(List<Float> existingReviews, Float newReview) {
        float reviewCount = existingReviews.size() + 1.0f;
        double sumOfExisitingReview = existingReviews.stream().filter(value -> value != null)
                .mapToDouble(Float::doubleValue).sum();
        return ((float) sumOfExisitingReview + newReview) / reviewCount;
    }

    public ResponseModel<ActivityDTO> buildActivityDTOResponse(Page<Activity> activityPage) {
        List<ActivityDTO> listOfActivities = activityPage.toList()
                .stream()
                .map(activityMapper::mapToActivityDto)
                .collect(Collectors.toList());
        return ResponseModel.<ActivityDTO>builder()
                .pageNo(activityPage.getNumber())
                .pageSize(activityPage.getSize())
                .totalElements(activityPage.getTotalElements())
                .totalPages(activityPage.getTotalPages())
                .data(listOfActivities)
                .isLastPage(activityPage.isLast())
                .build();
    }
}

