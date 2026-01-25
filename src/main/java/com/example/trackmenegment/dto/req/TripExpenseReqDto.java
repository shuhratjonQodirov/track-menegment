package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.TripExpenseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripExpenseReqDto {
    private Long tripId;

    private TripExpenseType type;

    private BigDecimal amountLocal;

    private String localCurrency;

    private BigDecimal amountUsd;

    private String description;

    private LocalDate expenseDate;
}
