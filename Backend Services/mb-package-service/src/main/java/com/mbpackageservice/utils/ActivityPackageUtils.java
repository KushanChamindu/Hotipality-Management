package com.mbpackageservice.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dao.mapper.ActivityPackagMapper;
import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.activitypackage.ActivityPackageDTO;
import com.mbpackageservice.model.activitypackage.ActivityPackage;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
public class ActivityPackageUtils {

    @Autowired
    private ActivityPackagMapper activityPackageMapper;

    public Float calculateAverageRaview(List<Float> existingReviews, Float newReview) {
        float reviewCount = existingReviews.size() + 1.0f;
        double sumOfExisitingReview = existingReviews.stream().filter(value -> value != null)
                .mapToDouble(Float::doubleValue).sum();
        return ((float) sumOfExisitingReview + newReview) / reviewCount;
    }

    public ResponseModel<ActivityPackageDTO> buildActivityPackageDTOResponse(
            Page<ActivityPackage> activityPackagePage) {
        List<ActivityPackageDTO> listOfActivitiePackages = activityPackagePage.toList()
                .stream()
                .map(activityPackageMapper::mapToActivityPackageDto)
                .collect(Collectors.toList());
        return ResponseModel.<ActivityPackageDTO>builder()
                .pageNo(activityPackagePage.getNumber())
                .pageSize(activityPackagePage.getSize())
                .totalElements(activityPackagePage.getTotalElements())
                .totalPages(activityPackagePage.getTotalPages())
                .data(listOfActivitiePackages)
                .isLastPage(activityPackagePage.isLast())
                .build();
    }

}
