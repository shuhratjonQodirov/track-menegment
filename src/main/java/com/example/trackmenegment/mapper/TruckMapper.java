package com.example.trackmenegment.mapper;

import com.example.trackmenegment.dto.req.TruckReqDto;
import com.example.trackmenegment.dto.res.TruckResDto;
import com.example.trackmenegment.model.Truck;
import org.springframework.stereotype.Component;

@Component
public class TruckMapper {

    public Truck toEntity(TruckReqDto truckReqDto) {
        return Truck.builder()
                .truckNumber(truckReqDto.getTruckNumber().toUpperCase())
                .modelName(truckReqDto.getModelName().toUpperCase())
                .manufactureYear(truckReqDto.getManufactureYear())
                .buyPrice(truckReqDto.getBuyPrice())
                .buyDate(truckReqDto.getBuyDate())
                .truckStatus(truckReqDto.getTruckStatus())
                .build();
    }

    public TruckResDto toDto(Truck truck) {
        return TruckResDto.builder()
                .id(truck.getId())
                .truckNumber(truck.getTruckNumber())
                .modelName(truck.getModelName())
                .manufactureYear(truck.getManufactureYear())
                .buyPrice(truck.getBuyPrice())
                .buyDate(truck.getBuyDate())
                .truckStatus(truck.getTruckStatus())
                .build();
    }
}
