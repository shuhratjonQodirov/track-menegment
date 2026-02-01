package com.example.trackmenegment.mapper;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.dto.res.UserBalanceResponse;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import org.springframework.stereotype.Component;

@Component
public class UserBalanceMapper {
    public UserBalance toEntity(UserBalanceReqDto dto, User user, Trip trip) {
        return UserBalance.builder()
                .user(user)
                .trip(trip)
                .operationDate(dto.getOperationDate())
                .isPaid(dto.getIsPaid())
                .description(dto.getDescription())
                .paymentType(dto.getPaymentType())
                .amountUsd(dto.getAmountUsd())
                .build();
    }

    public UserBalanceResponse toDto(UserBalance balance) {
        return UserBalanceResponse
                .builder()
                .id(balance.getId())
                .tripId(balance.getTrip() != null ? balance.getTrip().getId() : null)
                .userId(balance.getUser().getId())
                .operationDate(balance.getOperationDate())
                .paymentType(balance.getPaymentType())
                .amountUsd(balance.getAmountUsd())
                .description(balance.getDescription())
                .isPaid(balance.getIsPaid())
                .build();
    }

    public void toUpdateEntity(UserBalance userBalance, UserBalanceReqDto dto, Trip trip) {
        userBalance.setTrip(trip);
        userBalance.setOperationDate(dto.getOperationDate());
        userBalance.setIsPaid(dto.getIsPaid());
        userBalance.setDescription(dto.getDescription());
        userBalance.setPaymentType(dto.getPaymentType());
        userBalance.setAmountUsd(dto.getAmountUsd());
    }
}
