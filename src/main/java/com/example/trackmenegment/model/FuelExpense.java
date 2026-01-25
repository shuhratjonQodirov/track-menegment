package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.Currency;
import com.example.trackmenegment.utils.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class FuelExpense extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Truck truck;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @Column(nullable = false)
    private BigDecimal liters;

    @Column(nullable = false)
    private BigDecimal pricePerLiter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency localCurrency;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amountLocal;

    @Column(nullable = false)
    private BigDecimal totalAmountUsd;

    private String gasStationName;

    @Column(length = 100)
    private String country;

    private LocalDate fuelDate;
}
