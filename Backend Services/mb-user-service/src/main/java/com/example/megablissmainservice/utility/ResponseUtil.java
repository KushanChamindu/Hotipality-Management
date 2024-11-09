package com.example.megablissmainservice.utility;

import com.example.megablissmainservice.DTO.ResponseDto;
import org.springframework.stereotype.Component;

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
