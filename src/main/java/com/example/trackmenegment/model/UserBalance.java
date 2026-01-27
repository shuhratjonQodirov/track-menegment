package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.PaymentType;
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
public class UserBalance extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    private BigDecimal amountUsd;             // +1500 yoki -800

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    private LocalDate operationDate;

    private Boolean isPaid;
}
