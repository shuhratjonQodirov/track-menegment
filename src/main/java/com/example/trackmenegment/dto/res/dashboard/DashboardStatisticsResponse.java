package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatisticsResponse {
    // ===== 1. ASOSIY STATISTIKA =====
    private Long totalTrips;              // Jami reyslar soni
    private Long inProgressTrips;         // Jarayondagi reyslar
    private Long completedTrips;          // Tugallangan reyslar
    private Long activeTrucks;            // Faol mashinalar
    private Long totalTrucks;             // Jami mashinalar
    private Long totalUsers;              // Jami xodimlar
    private Long totalDrivers;            // Jami haydovchilar

    // ===== 2. MOLIYAVIY KO'RSATKICHLAR =====
    private BigDecimal totalIncome;       // Jami daromad (incomeOut + incomeBack)
    private BigDecimal totalExpense;      // Jami xarajat (fuel + truck + trip + salary)
    private BigDecimal netProfit;         // Sof foyda (income - expense)

    // Xarajatlar tarkibi
    private BigDecimal fuelExpense;       // Yoqilg'i xarajati
    private BigDecimal truckExpense;      // Mashina ta'mirlash xarajati
    private BigDecimal tripExpense;       // Yo'l xarajati (road tax, parking, etc.)
    private BigDecimal salaryExpense;     // Oyliklar xarajati

    // ===== 3. OYLIK STATISTIKA =====
    private Long monthCompletedTrips;     // Ushbu oyda tugallangan reyslar
    private BigDecimal monthIncome;       // Ushbu oy daromadi
    private BigDecimal monthExpense;      // Ushbu oy xarajati

    // ===== 4. BUGUNGI KO'RSATKICHLAR =====
    private Long todayNewTrips;           // Bugun boshlangan reyslar
    private BigDecimal todayFuel;         // Bugun sarflangan yoqilg'i (litr)
    private BigDecimal todayExpense;      // Bugungi xarajat

    // ===== 5. O'RTACHA KO'RSATKICHLAR =====
    private Double avgTripDuration;       // O'rtacha reys davomiyligi (kun)
    private BigDecimal avgTripIncome;     // O'rtacha reys daromadi
    private BigDecimal avgFuelPerTrip;    // O'rtacha yoqilg'i sarfi (litr/reys)

    // ===== 6. QARZDORLIK HOLATI =====
    private BigDecimal unpaidSalaries;    // To'lanmagan oyliklar
    private BigDecimal paidSalaries;      // To'langan oyliklar
    private BigDecimal totalDebt;         // Jami qarz (barcha xodimlar)
    private Long unpaidDriversCount;      // Qarzdor haydovchilar soni

    // ===== 7. RO'YXATLAR =====
    private List<TopDriverDto> topDrivers;             // Top 5 haydovchilar
    private List<RecentTripDto> recentTrips;           // Oxirgi 5 reys
    private List<TruckStatusDto> truckStatusList;      // Mashinalar holati
    private List<DebtorDto> debtorsList;               // Qarzdorlar ro'yxati

    // ===== 8. CHART DATA =====
    private ChartData chartData;
}
