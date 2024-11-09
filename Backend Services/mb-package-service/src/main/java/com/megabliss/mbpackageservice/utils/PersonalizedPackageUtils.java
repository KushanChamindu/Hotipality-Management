package com.megabliss.mbpackageservice.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.megabliss.mbpackageservice.dao.mapper.PersonalizedPackagMapper;
import com.megabliss.mbpackageservice.dto.ResponseModel;
import com.megabliss.mbpackageservice.dto.personalizedpackage.PersonalizedPackageDTO;
import com.megabliss.mbpackageservice.model.personalizedpackage.PersonalizedPackage;

import lombok.Getter;

@Component
@Getter
public class PersonalizedPackageUtils {
    @Autowired
    private PersonalizedPackagMapper personalizedPackagMapper;

    public ResponseModel<PersonalizedPackageDTO> buildPersonalizedPackageDTOResponse(Page<PersonalizedPackage> personalizedPackagePage) {
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
