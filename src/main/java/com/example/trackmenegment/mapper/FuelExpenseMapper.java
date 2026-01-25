package com.example.trackmenegment.mapper;

import com.example.trackmenegment.dto.req.FuelExpenseReqDto;
import com.example.trackmenegment.dto.res.FuelExpenseResDto;
import com.example.trackmenegment.model.FuelExpense;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import org.springframework.stereotype.Component;

@Component
public class FuelExpenseMapper {

    public FuelExpense toEntity(FuelExpenseReqDto fuelDto,
                                Truck truck,
                                Trip trip) {
        return FuelExpense.builder()
                .truck(truck)
                .trip(trip).
                liters(fuelDto.getLitres())
                .pricePerLiter(fuelDto.getPricePerLitres())
                .localCurrency(fuelDto.getLocalCurrency())
                .amountLocal(fuelDto.getAmountLocal())
                .totalAmountUsd(fuelDto.getTotalAmountUsd())
                .gasStationName(fuelDto.getGasStationName())
                .country(fuelDto.getCountry())
                .fuelDate(fuelDto.getFuelDate())
                .build();
    }

    public FuelExpenseResDto toDto(FuelExpense fuel) {
        return FuelExpenseResDto.builder().id(fuel.getId())
                .fuelDate(fuel.getFuelDate())
                .location(fuel.getCountry())
                .liters(fuel.getLiters())
                .pricePerLiterUsd(fuel.getPricePerLiter())
                .totalAmountUsd(fuel.getTotalAmountUsd())
                .gasStationName(fuel.getGasStationName())
                .localCurrency(fuel.getLocalCurrency().name())
                .amountLocal(fuel.getAmountLocal())
                .country(fuel.getCountry())
                .build();
    }
}
