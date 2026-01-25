package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckExpenseReq {

    private Long truckId;

    private Long tripId;

    private String truckExpenseType;

    private BigDecimal amountUsd;

    private BigDecimal amountLocal;

    private String localCurrency;

    private String description;

    private LocalDate expenseDate;
}
