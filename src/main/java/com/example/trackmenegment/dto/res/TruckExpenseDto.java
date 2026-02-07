package com.example.trackmenegment.dto.res;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckExpenseDto {
    private List<TruckExpenseResDto> list;
    private BigDecimal totalAmountByTrip;
    private BigDecimal totalAmountByGarage;
}
