package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.dto.res.dashboard.*;
import com.example.trackmenegment.enums.TripStatus;
import com.example.trackmenegment.enums.TruckStatus;
import com.example.trackmenegment.model.*;
import com.example.trackmenegment.repository.*;
import com.example.trackmenegment.service.DashboardService;
import com.example.trackmenegment.utils.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final TripRepository tripRepository;
    private final TruckRepository truckRepository;
    private final UserRepository userRepository;
    private final FuelExpenseRepository fuelExpenseRepository;
    private final TripExpenseRepository tripExpenseRepository;
    private final TruckExpenseRepository truckExpenseRepository;
    private final UserBalanceRepository userBalanceRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getStatistic() {  // ===== 1. ASOSIY STATISTIKA =====
    Long totalTrips = tripRepository.count();
    Long inProgressTrips = tripRepository.countByTripStatusAndDeletedFalse(TripStatus.IN_PROGRESS);
    Long completedTrips = tripRepository.countByTripStatusAndDeletedFalse(TripStatus.COMPLETED);

    Long totalTrucks = truckRepository.count();
    Long activeTrucks = truckRepository.countByTruckStatusAndDeletedFalse(TruckStatus.ACTIVE);

    Long totalUsers = userRepository.count();
    Long totalDrivers = userRepository.countByRolesAndDeletedFalse(com.example.trackmenegment.enums.UserRoles.DRIVER);

    // ===== 2. MOLIYAVIY KO'RSATKICHLAR =====
    List<Trip> allTrips = tripRepository.findAllByDeletedFalse();

    // Jami daromad
    BigDecimal totalIncome = allTrips.stream()
            .map(trip -> trip.getIncomeOutUsd().add(trip.getIncomeBackUsd()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Yoqilg'i xarajati
    BigDecimal fuelExpense = fuelExpenseRepository.findAll().stream()
            .map(FuelExpense::getTotalAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Mashina ta'mirlash xarajati
    BigDecimal truckExpense = truckExpenseRepository.findAll().stream()
            .map(TruckExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Yo'l xarajati
    BigDecimal tripExpense = tripExpenseRepository.findAll().stream()
            .map(TripExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Oyliklar xarajati
    BigDecimal salaryExpense = allTrips.stream()
            .map(Trip::getDriverIncomeUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Jami xarajat
    BigDecimal totalExpense = fuelExpense
            .add(truckExpense)
            .add(tripExpense)
            .add(salaryExpense);

    // Sof foyda
    BigDecimal netProfit = totalIncome.subtract(totalExpense);

    // ===== 3. OYLIK STATISTIKA =====
    LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
    LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(
            LocalDate.now().lengthOfMonth()
    );

    Long monthCompletedTrips = tripRepository.countByTripStatusAndFinishedDateBetweenAndDeletedFalse(
            TripStatus.COMPLETED, firstDayOfMonth, lastDayOfMonth
    );

    List<Trip> monthTrips = tripRepository.findByFinishedDateBetweenAndDeletedFalse(
            firstDayOfMonth, lastDayOfMonth
    );

    BigDecimal monthIncome = monthTrips.stream()
            .map(trip -> trip.getIncomeOutUsd().add(trip.getIncomeBackUsd()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal monthExpense = calculateMonthExpenses(firstDayOfMonth, lastDayOfMonth);

    // ===== 4. BUGUNGI KO'RSATKICHLAR =====
    LocalDate today = LocalDate.now();

    Long todayNewTrips = tripRepository.countByStartedDateAndDeletedFalse(today);

    BigDecimal todayFuel = fuelExpenseRepository.findByFuelDateAndDeletedFalse(today).stream()
            .map(FuelExpense::getLiters)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal todayExpense = calculateTodayExpenses(today);

    // ===== 5. O'RTACHA KO'RSATKICHLAR =====
    Double avgTripDuration = tripRepository.findAll().stream()
            .filter(trip -> trip.getTripDuration() > 0)
            .mapToInt(Trip::getTripDuration)
            .average()
            .orElse(0.0);

    BigDecimal avgTripIncome = totalTrips > 0
            ? totalIncome.divide(BigDecimal.valueOf(totalTrips), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

    BigDecimal totalFuelLiters = fuelExpenseRepository.findAll().stream()
            .map(FuelExpense::getLiters)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal avgFuelPerTrip = totalTrips > 0
            ? totalFuelLiters.divide(BigDecimal.valueOf(totalTrips), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

    // ===== 6. QARZDORLIK HOLATI =====
    List<Trip> unpaidTrips = tripRepository.findByDriverIncomePaidFalseAndDeletedFalse();

    BigDecimal unpaidSalaries = unpaidTrips.stream()
            .map(Trip::getDriverIncomeUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal paidSalaries = tripRepository.findByDriverIncomePaidTrueAndFinishedDateBetweenAndDeletedFalse(
                    firstDayOfMonth, lastDayOfMonth
            ).stream()
            .map(Trip::getDriverIncomeUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalDebt = userBalanceRepository.findAll().stream()
            .filter(balance -> !balance.getIsPaid())
            .map(UserBalance::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Long unpaidDriversCount = (long) unpaidTrips.stream()
            .map(Trip::getUser)
            .distinct()
            .count();

    // ===== 7. RO'YXATLAR =====
    List<TopDriverDto> topDrivers = getTopDrivers();
    List<RecentTripDto> recentTrips = getRecentTrips();
    List<TruckStatusDto> truckStatusList = getTruckStatusList();
    List<DebtorDto> debtorsList = getDebtorsList();

    // ===== 8. CHART DATA =====
    ChartData chartData = ChartData.builder()
            .totalIncome(totalIncome)
            .fuelExpense(fuelExpense)
            .truckExpense(truckExpense)
            .tripExpense(tripExpense)
            .salaryExpense(salaryExpense)
            .netProfit(netProfit)
            .build();


        DashboardStatisticsResponse build = DashboardStatisticsResponse.builder()
                .totalTrips(totalTrips)
                .inProgressTrips(inProgressTrips)
                .completedTrips(completedTrips)
                .activeTrucks(activeTrucks)
                .totalTrucks(totalTrucks)
                .totalUsers(totalUsers)
                .totalDrivers(totalDrivers)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netProfit(netProfit)
                .fuelExpense(fuelExpense)
                .truckExpense(truckExpense)
                .tripExpense(tripExpense)
                .salaryExpense(salaryExpense)
                .monthCompletedTrips(monthCompletedTrips)
                .monthIncome(monthIncome)
                .monthExpense(monthExpense)
                .todayNewTrips(todayNewTrips)
                .todayFuel(todayFuel)
                .todayExpense(todayExpense)
                .avgTripDuration(avgTripDuration)
                .avgTripIncome(avgTripIncome)
                .avgFuelPerTrip(avgFuelPerTrip)
                .unpaidSalaries(unpaidSalaries)
                .paidSalaries(paidSalaries)
                .totalDebt(totalDebt)
                .unpaidDriversCount(unpaidDriversCount)
                .topDrivers(topDrivers)
                .recentTrips(recentTrips)
                .truckStatusList(truckStatusList)
                .debtorsList(debtorsList)
                .chartData(chartData)
                .build();
        return new ApiResponse("Dashboard statistikasi yuklandi",true,build);
    }

// ===== HELPER METHODS =====

private BigDecimal calculateMonthExpenses(LocalDate start, LocalDate end) {
    BigDecimal fuel = fuelExpenseRepository.findByFuelDateBetweenAndDeletedFalse(start, end).stream()
            .map(FuelExpense::getTotalAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal truck = truckExpenseRepository.findByExpenseDateBetweenAndDeletedFalse(start, end).stream()
            .map(TruckExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal trip = tripExpenseRepository.findByExpenseDateBetweenAndDeletedFalse(start, end).stream()
            .map(TripExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return fuel.add(truck).add(trip);
}

private BigDecimal calculateTodayExpenses(LocalDate today) {
    BigDecimal fuel = fuelExpenseRepository.findByFuelDateAndDeletedFalse(today).stream()
            .map(FuelExpense::getTotalAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal truck = truckExpenseRepository.findByExpenseDateAndDeletedFalse(today).stream()
            .map(TruckExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal trip = tripExpenseRepository.findByExpenseDateAndDeletedFalse(today).stream()
            .map(TripExpense::getAmountUsd)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return fuel.add(truck).add(trip);
}

private List<TopDriverDto> getTopDrivers() {
    return userRepository.findByRolesAndDeletedFalse(com.example.trackmenegment.enums.UserRoles.DRIVER)
            .stream()
            .map(driver -> {
                List<Trip> driverTrips = tripRepository.findByUserAndTripStatusAndDeletedFalse(
                        driver, TripStatus.COMPLETED
                );

                BigDecimal totalIncome = driverTrips.stream()
                        .map(trip -> trip.getIncomeOutUsd().add(trip.getIncomeBackUsd()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                return TopDriverDto.builder()
                        .userId(driver.getId())
                        .fullName(driver.getFullName())
                        .completedTrips((long) driverTrips.size())
                        .totalIncome(totalIncome)
                        .totalProfit(totalIncome) // TODO: Hisoblash kerak
                        .build();
            })
            .sorted((a, b) -> b.getTotalIncome().compareTo(a.getTotalIncome()))
            .limit(5)
            .collect(Collectors.toList());
}

private List<RecentTripDto> getRecentTrips() {
    return tripRepository.findTop5ByOrderByStartedDateDesc()
            .stream()
            .map(trip -> RecentTripDto.builder()
                    .tripId(trip.getId())
                    .routeFrom(trip.getRouteFrom())
                    .routeTo(trip.getRouteTo())
                    .driverFullName(trip.getUser() != null ? trip.getUser().getFullName() : "Noma'lum")
                    .truckNumber(trip.getTruck() != null ? trip.getTruck().getTruckNumber() : "-")
                    .tripStatus(trip.getTripStatus().name())
                    .startedDate(trip.getStartedDate() != null ? trip.getStartedDate().toString() : "-")
                    .totalIncomeUsd(trip.getIncomeOutUsd().add(trip.getIncomeBackUsd()))
                    .build())
            .collect(Collectors.toList());
}

private List<TruckStatusDto> getTruckStatusList() {
    return truckRepository.findAll().stream()
            .map(truck -> TruckStatusDto.builder()
                    .truckId(truck.getId())
                    .truckNumber(truck.getTruckNumber())
                    .modelName(truck.getModelName())
                    .manufactureYear(truck.getManufactureYear())
                    .truckStatus(truck.getTruckStatus().name())
                    .build())
            .collect(Collectors.toList());
}

private List<DebtorDto> getDebtorsList() {
    return tripRepository.findByDriverIncomePaidFalseAndDeletedFalse().stream()
            .collect(Collectors.groupingBy(
                    Trip::getUser,
                    Collectors.reducing(
                            BigDecimal.ZERO,
                            Trip::getDriverIncomeUsd,
                            BigDecimal::add
                    )
            ))
            .entrySet().stream()
            .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0)
            .map(entry -> DebtorDto.builder()
                    .userId(entry.getKey().getId())
                    .fullName(entry.getKey().getFullName())
                    .debt(entry.getValue())
                    .build())
            .sorted((a, b) -> b.getDebt().compareTo(a.getDebt()))
            .collect(Collectors.toList());
}
}
