package com.mbpackageservice.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dao.mapper.PersonalizedPackagMapper;
import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.mbpackageservice.model.personalizedpackage.PersonalizedPackage;

import lombok.Getter;

@Component
@Getter
public class PersonalizedPackageUtils {
    @Autowired
    private PersonalizedPackagMapper personalizedPackagMapper;

    public ResponseModel<PersonalizedPackageDTO> buildPersonalizedPackageDTOResponse(
            Page<PersonalizedPackage> personalizedPackagePage) {
        List<PersonalizedPackageDTO> listOfPersonalizedPackages = personalizedPackagePage.toList()
                .stream()
                .map(personalizedPackagMapper::mapToPersonalizedPackageDto)
                .collect(Collectors.toList());
        return ResponseModel.<PersonalizedPackageDTO>builder()
                .pageNo(personalizedPackagePage.getNumber())
                .pageSize(personalizedPackagePage.getSize())
                .totalElements(personalizedPackagePage.getTotalElements())
                .totalPages(personalizedPackagePage.getTotalPages())
                .data(listOfPersonalizedPackages)
                .isLastPage(personalizedPackagePage.isLast())
                .build();
    }
}
