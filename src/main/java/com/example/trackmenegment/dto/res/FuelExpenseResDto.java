package com.example.trackmenegment.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelExpenseResDto {
    private Long id;
    private LocalDate fuelDate;         // 2025-06-15
    private String location;            // "Toshkent", "Moskva", "Chegara"
    private BigDecimal liters;             // 800
    private BigDecimal pricePerLiterUsd;// 1.08
    private BigDecimal totalAmountUsd;  // 864.00
    private String gasStationName;      // "Lukoil", "Gazprom" (ixtiyoriy)
    private String localCurrency;
    private BigDecimal amountLocal;
    private String country;
}
