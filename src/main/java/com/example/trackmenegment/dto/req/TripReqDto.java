package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripReqDto {

    private Long truckId;

    private Long driverId;

    private String routeFrom;

    private String routeTo;

    private BigDecimal incomeOutUsd;

    private BigDecimal incomeBackUsd;

    private BigDecimal driverIncomeUsd;

    private Boolean driverIncomePaid = false;

    private TripStatus tripStatus;

    private LocalDate startedDate;

    private LocalDate finishedDate;

    private int tripDuration;
}
