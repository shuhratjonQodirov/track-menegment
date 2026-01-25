package com.example.trackmenegment.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime occurred;
    private String message;
    private int status;
}

