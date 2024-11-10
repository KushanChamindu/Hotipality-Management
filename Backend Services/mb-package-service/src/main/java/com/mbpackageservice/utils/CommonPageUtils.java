package com.mbpackageservice.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mbpackageservice.dto.ResponseModel;

@Component
public class CommonPageUtils {

    public <T> ResponseModel<T> buildPagedResponseModel(Page<T> pageData) {
        return ResponseModel.<T>builder()
                .pageNo(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent())
                .isLastPage(pageData.isLast())
                .build();
    }
}
