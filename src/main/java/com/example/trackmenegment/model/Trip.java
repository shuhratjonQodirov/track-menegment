package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.TripStatus;
import com.example.trackmenegment.utils.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Trip extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Truck truck;
    /// 40|366PAA

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    /// dastonbek

    private String routeFrom;       // "Toshkent"

    private String routeTo;         // "Moskva"

    private BigDecimal incomeOutUsd;
    /// 3600$ yo'l haqi chiqib ketdi

    private BigDecimal incomeBackUsd;
    /// 4500$ qaytdi

    @Column(nullable = false)
    private BigDecimal driverIncomeUsd;

    private boolean driverIncomePaid; // To‘landimi?

    private LocalDate startedDate;

    private LocalDate finishedDate;

    private int tripDuration;

    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus = TripStatus.IN_PROGRESS;

    @PrePersist
    protected void onCreate() {
        tripDuration = calculateDuration(startedDate, finishedDate);
        if (incomeBackUsd == null) {
            incomeBackUsd = BigDecimal.ZERO;
        }

    }

    private int calculateDuration(LocalDate start, LocalDate finishedDate) {
        return finishedDate != null && start != null ? (int) ChronoUnit.DAYS.between(start, finishedDate) : 0;
    }

    public String getReysCode() {
        if (getId() == null) return null;

        int year = startedDate.getYear();              // to'liq yil: 2025
        String yearShort = String.format("%02d", year % 100);
        return getId() + "-SHMQ/" + yearShort;
    }


}
