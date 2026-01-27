package com.example.trackmenegment.service;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import com.example.trackmenegment.utils.ApiResponse;

import java.math.BigDecimal;

public interface UserBalanceService {
    void createDriverBalance(User user, Trip trip, BigDecimal amountUsd, Boolean isPaid);

    ApiResponse create(UserBalanceReqDto dto);

    ApiResponse getAll(Long userId);
}
