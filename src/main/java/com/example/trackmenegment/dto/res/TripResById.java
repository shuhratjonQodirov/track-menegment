package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.TripStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResById {
    private Long id;

    /// truck data
    private Long truckId;
    private String truckNumber;

    /// driver data
    private Long driverId;
    private String driverFullName;

    /// road data
    private String routeFrom;
    private String routeTo;

    /// ketish va qaytish pullari
    private BigDecimal incomeOutUsd;
    private BigDecimal incomeBackUsd;
    private BigDecimal totalIncomeUsd;

    /// trip expenses
    private BigDecimal totalTripExpensesUsd;
    private List<TripExpenseResDto> tripExpenseResDto;

    /// truck expenses
    private BigDecimal truckExpensesTotalUsd;
    private List<TruckExpenseResDto> truckExpenses;
    /// fuel expenses
    private BigDecimal totalLiters;
    private BigDecimal totalFuelCostUsd;
    private List<FuelExpenseResDto> fuelEntries;

    // Haydovchi maoshi va harajatlari
    private BigDecimal driverIncomeUsd;
    private boolean driverIncomePaid;

    List<UserBalanceResponse> userBalanceResponseList;
    private BigDecimal personalExpenseDriver;

    // Hisob-kitoblar
    private BigDecimal profitUsd;
private BigDecimal totalExpense;
    private BigDecimal giveDriverUsd;

    private TripStatus tripStatus;

    private LocalDate startedDate;

    private LocalDate finishedDate;

    private int tripDuration;

}
