package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.TruckStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckReqDto {
    private String truckNumber;
    private String modelName;
    private int manufactureYear;
    private BigDecimal buyPrice;
    private LocalDate buyDate;
    private TruckStatus truckStatus;
}
