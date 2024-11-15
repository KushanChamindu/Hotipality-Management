package com.mbpackageservice.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dao.mapper.PackageBusinessMapper;
import com.mbpackageservice.dto.ResponseModel;
import com.mbpackageservice.dto.packagebusiness.PackageBusinessDTO;
import com.mbpackageservice.model.packagebusiness.PackageBusiness;

import lombok.Getter;

@Component
@Getter
public class PackageBusinessUtils {

    @Autowired
    private PackageBusinessMapper packageBusinessMapper;

    public ResponseModel<PackageBusinessDTO> buildActivityDTOResponse(Page<PackageBusiness> packageBusinessPage) {
        List<PackageBusinessDTO> listOfPackageBusinesses = packageBusinessPage.toList()
                .stream()
                .map(packageBusinessMapper::mapToPackageBusinessDto)
                .collect(Collectors.toList());
        return ResponseModel.<PackageBusinessDTO>builder()
                .pageNo(packageBusinessPage.getNumber())
                .pageSize(packageBusinessPage.getSize())
                .totalElements(packageBusinessPage.getTotalElements())
                .totalPages(packageBusinessPage.getTotalPages())
                .data(listOfPackageBusinesses)
                .isLastPage(packageBusinessPage.isLast())
                .build();
    }
}
