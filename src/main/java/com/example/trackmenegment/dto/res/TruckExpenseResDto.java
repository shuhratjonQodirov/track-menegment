package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.TruckExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckExpenseResDto {
    private Long id;
    private Long truckId;
    private Long tripId;
    private TruckExpenseType truckExpenseType;
    private String localCurrency;
    private BigDecimal amountLocal;
    private BigDecimal amountUsd;
    private String description;
    private LocalDate expenseDate;
}
