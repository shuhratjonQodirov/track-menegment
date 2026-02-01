package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBalanceResponse {
    private Long id;
    private Long userId;
    private Long tripId;
    private BigDecimal amountUsd;
    private PaymentType paymentType;
    private String description;
    private LocalDate operationDate;
    private boolean isPaid;
}
