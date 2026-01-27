package com.example.trackmenegment.dto.req;

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
public class UserBalanceReqDto {

    private Long userId;
    private Long tripId;
    private BigDecimal amountUsd;
    private PaymentType paymentType;
    private String description;
    private LocalDate operationDate;
    private Boolean isPaid;
}
