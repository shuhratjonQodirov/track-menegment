package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class DebtorDto {
    private Long userId;
    private String fullName;
    private BigDecimal debt;
}