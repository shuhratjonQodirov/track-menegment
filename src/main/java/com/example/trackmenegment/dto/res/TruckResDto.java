package com.example.trackmenegment.dto.res;

import com.example.trackmenegment.enums.TruckStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckResDto {
    private Long id;
    private String truckNumber;
    private String modelName;
    private int manufactureYear;
    private BigDecimal buyPrice;
    private LocalDate buyDate;
    private TruckStatus truckStatus;
}
