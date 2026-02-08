package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartData {
    private BigDecimal totalIncome;
    private BigDecimal fuelExpense;
    private BigDecimal truckExpense;
    private BigDecimal tripExpense;
    private BigDecimal salaryExpense;
    private BigDecimal netProfit;
}