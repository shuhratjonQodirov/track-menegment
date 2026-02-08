package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class TopDriverDto {
    private Long userId;
    private String fullName;
    private Long completedTrips;
    private BigDecimal totalIncome;
    private BigDecimal totalProfit;
}