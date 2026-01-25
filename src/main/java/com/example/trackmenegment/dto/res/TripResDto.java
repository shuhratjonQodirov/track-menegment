package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripResDto {

    private Long id;

    private String truckNumber;

    private String fullName;

    private String routeFrom;

    private String routeTo;

    private BigDecimal incomeOutUsd;

    private BigDecimal incomeBackUsd;

    private String tripStatus;

    private int tripDuration;
}
