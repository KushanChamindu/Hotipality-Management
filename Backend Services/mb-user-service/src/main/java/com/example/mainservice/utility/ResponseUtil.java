package com.example.mainservice.utility;

import org.springframework.stereotype.Component;

import com.example.mainservice.DTO.ResponseDto;

@Component
public class ResponseUtil {
    public ResponseDto createResponse(String response, String status, String comment) {
        return ResponseDto.builder()
                .response(response)
                .status(status)
                .comment(comment)
                .build();
    }
}
