package com.example.trackmenegment.dto.res.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class TruckStatusDto {
    private Long truckId;
    private String truckNumber;
    private String modelName;
    private Integer manufactureYear;
    private String truckStatus;
}