package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.Currency;
import com.example.trackmenegment.enums.TruckExpenseType;
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
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TruckExpense extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Truck truck;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckExpenseType truckExpenseType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountUsd;

    private BigDecimal amountLocal;

    @Enumerated(EnumType.STRING)
    private Currency localCurrency;

    private String description;

    private LocalDate expenseDate;

}
