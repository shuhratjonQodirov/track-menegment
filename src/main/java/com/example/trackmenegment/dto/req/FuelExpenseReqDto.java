package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelExpenseReqDto {
    private Long tripId;

    private Long truckId;

    private BigDecimal litres;

    private BigDecimal PricePerLitres;

    private Currency localCurrency;

    private BigDecimal amountLocal;

    private BigDecimal totalAmountUsd;

    private String gasStationName;

    private String country;

    private LocalDate fuelDate;
}
