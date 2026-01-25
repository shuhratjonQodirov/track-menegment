package com.example.trackmenegment.service;

import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;

import java.math.BigDecimal;

public interface UserBalanceService {
    UserBalance createDriverBalance(User user, Trip trip, BigDecimal amountUsd, Boolean isPaid);
}
