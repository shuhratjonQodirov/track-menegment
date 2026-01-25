package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.TripExpenseType;
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
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TripExpense extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private Trip trip;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TripExpenseType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amountLocal;

    @Column(nullable = false, length = 10)
    private String localCurrency;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountUsd;

    private String description;

    private LocalDate expenseDate;

}
