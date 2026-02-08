package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentTripDto {
    private Long tripId;
    private String routeFrom;
    private String routeTo;
    private String driverFullName;
    private String truckNumber;
    private String tripStatus;
    private String startedDate;
    private BigDecimal totalIncomeUsd;
}