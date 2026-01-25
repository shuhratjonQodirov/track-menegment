package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.enums.PaymentType;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import com.example.trackmenegment.repository.UserBalanceRepository;
import com.example.trackmenegment.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserBalanceRepository userBalanceRepository;

    @Override
    public UserBalance createDriverBalance(User user, Trip trip, BigDecimal amountUsd, Boolean isPaid) {
        UserBalance balance = UserBalance.builder()
                .user(user)
                .trip(trip)
                .amountUsd(amountUsd)
                .paymentType(PaymentType.SALARY) // default ish haqi
                .description("Oylik ish haqi")
                .isPaid(isPaid)
                .build();
        return userBalanceRepository.save(balance);
    }
}
