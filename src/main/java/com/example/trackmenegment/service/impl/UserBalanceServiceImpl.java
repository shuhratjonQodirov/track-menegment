package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.dto.res.UserBalanceResponse;
import com.example.trackmenegment.enums.PaymentType;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.mapper.UserBalanceMapper;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import com.example.trackmenegment.repository.TripRepository;
import com.example.trackmenegment.repository.UserBalanceRepository;
import com.example.trackmenegment.repository.UserRepository;
import com.example.trackmenegment.service.UserBalanceService;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.UserBalanceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserBalanceRepository userBalanceRepository;
    private final UserRepository userRepository;
    private final UserBalanceValidator validator;
    private final UserBalanceMapper mapper;
    private final TripRepository tripRepository;

    @Override
    public UserBalance createDriverBalance(User user, Trip trip, BigDecimal amountUsd, Boolean isPaid) {
        UserBalance balance = UserBalance.builder()
                .user(user)
                .trip(trip)
                .amountUsd(amountUsd)
                .paymentType(PaymentType.SALARY) // default ish haqi
                .description("Oylik ish haqi")
                .isPaid(isPaid)
                .operationDate(LocalDate.now())
                .build();
        return userBalanceRepository.save(balance);
    }

    @Override
    public ApiResponse create(UserBalanceReqDto dto) {

        validator.validateForCreate(dto);

        User user = userRepository.findByIdAndDeletedFalse(dto.getUserId()).orElseThrow(() -> new ByIdException("User not found"));
        Trip trip = null;
        if (dto.getTripId() != null) {
            trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId()).orElseThrow(() -> new ByIdException("trip not found"));
        }
        UserBalance balance = mapper.toEntity(dto, user, trip);
        userBalanceRepository.save(balance);
        return new ApiResponse(
                "balance successfully created", true
        );
    }


    @Override
    public ApiResponse getAll(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new ByIdException("User not found"));
        List<UserBalanceResponse> list = userBalanceRepository.findAllByUserAndDeletedFalse(user).stream()
                .map(mapper::toDto)
                .toList();
        return new ApiResponse("List of user balance", true, list);
    }
}
