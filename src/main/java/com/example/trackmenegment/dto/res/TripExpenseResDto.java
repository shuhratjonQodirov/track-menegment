package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.TripExpenseType;
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
public class TripExpenseResDto {
    private Long id;

    private TripExpenseType type;

    private BigDecimal amountLocal;

    private String localCurrency;

    private BigDecimal amountUsd;

    private String description;

    private LocalDate expenseDate;
}
