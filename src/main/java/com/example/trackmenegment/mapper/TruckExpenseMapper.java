package com.example.trackmenegment.mapper;

import com.example.trackmenegment.dto.req.TruckExpenseReq;
import com.example.trackmenegment.dto.res.TruckExpenseResDto;
import com.example.trackmenegment.enums.Currency;
import com.example.trackmenegment.enums.TruckExpenseType;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.model.TruckExpense;
import org.springframework.stereotype.Component;

@Component
public class TruckExpenseMapper {

    public TruckExpense toEntity(TruckExpenseReq expenseReq, Truck truck, Trip trip) {
        return TruckExpense.builder()
                .truck(truck)
                .trip(trip)
                .truckExpenseType(TruckExpenseType.valueOf(expenseReq.getTruckExpenseType()))
                .amountUsd(expenseReq.getAmountUsd())
                .amountLocal(expenseReq.getAmountLocal())
                .localCurrency(Currency.valueOf(expenseReq.getLocalCurrency()))
                .description(expenseReq.getDescription())
                .expenseDate(expenseReq.getExpenseDate())
                .build();
    }

    public TruckExpenseResDto toDto(TruckExpense exp) {
        return TruckExpenseResDto.builder()
                .id(exp.getId())
                .truckId(exp.getTruck().getId())
                .tripId(exp.getTrip() == null ? null : exp.getTrip().getId())
                .truckExpenseType(exp.getTruckExpenseType())
                .localCurrency(String.valueOf(exp.getLocalCurrency()))
                .amountLocal(exp.getAmountLocal())
                .amountUsd(exp.getAmountUsd())
                .description(exp.getDescription())
                .expenseDate(exp.getExpenseDate())
                .build();
    }

}
