package com.example.trackmenegment.service;

import com.example.trackmenegment.dto.req.TripExpenseReqDto;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.TripExpense;
import com.example.trackmenegment.repository.TripExpenseRepository;
import com.example.trackmenegment.repository.TripRepository;
import com.example.trackmenegment.utils.ApiResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TripExpenseService {

    private final TripExpenseRepository tripExpenseRepository;
    private final TripRepository tripRepository;

    public ApiResponse create(TripExpenseReqDto dto) {
        validateDto(dto);
        Trip trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId()).orElseThrow(() -> new ByIdException("Trip not found"));
        TripExpense expense = TripExpense.builder()
                .trip(trip)
                .type(dto.getType())
                .amountLocal(dto.getAmountLocal())
                .localCurrency(dto.getLocalCurrency())
                .amountUsd(dto.getAmountUsd())
                .description(dto.getDescription())
                .expenseDate(dto.getExpenseDate())
                .build();
        tripExpenseRepository.save(expense);
        return new ApiResponse("Trip expense successfully saved", true);
    }

    private void validateDto(TripExpenseReqDto dto) {

        if (dto.getType() == null) {
            throw new ByIdException("Expense type majburiy");
        }

        if (dto.getAmountLocal() == null || dto.getAmountLocal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Local amount noto‘g‘ri");
        }

        if (StringUtils.isBlank(dto.getLocalCurrency())) {
            throw new ByIdException("Local currency bo‘sh bo‘lishi mumkin emas");
        }

        if (dto.getAmountUsd() != null && dto.getAmountUsd().compareTo(BigDecimal.ZERO) < 0) {
            throw new ByIdException("USD amount manfiy bo‘lishi mumkin emas");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new ByIdException("Description majburiy");
        }

        if (dto.getExpenseDate() == null) {
            throw new ByIdException("Expense date majburiy");
        }
    }

    public ApiResponse getByTripId(Long tripId) {
        return null;
    }

    public ApiResponse update(Long id, TripExpenseReqDto dto) {
        TripExpense expense = tripExpenseRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Trip expense not found"));

        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId()).orElseThrow(() -> new ByIdException("Trip not found"));
            expense.setTrip(trip);
        }
        if (dto.getType() != null) {
            expense.setType(dto.getType());
        }
        if (dto.getDescription() != null) {
            expense.setDescription(dto.getDescription());
        }
        if (dto.getLocalCurrency() != null) {
            expense.setLocalCurrency(dto.getLocalCurrency());
        }
        if (!dto.getExpenseDate().isAfter(LocalDate.now())) {
            expense.setExpenseDate(dto.getExpenseDate());
        }
        if (dto.getAmountUsd() == null || dto.getAmountUsd().compareTo(BigDecimal.ZERO) == 0) {
            throw new ByIdException("Xarajat summasi 0 bo'lishi mumkin emas");
        }
        if (dto.getAmountLocal() == null || dto.getAmountLocal().compareTo(BigDecimal.ZERO) == 0) {
            throw new ByIdException("Xarajat summasi 0 bo'lishi mumkin emas");
        }
        expense.setAmountUsd(dto.getAmountUsd());
        expense.setAmountLocal(dto.getAmountUsd());

        tripExpenseRepository.save(expense);

        return new ApiResponse("Trip expense successfully updated", true);
    }


    public ApiResponse delete(Long id) {
        TripExpense expense = tripExpenseRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Trip expense not found"));
        expense.setDeleted(true);
        tripExpenseRepository.save(expense);
        return new ApiResponse("Trip expense successfully deleted", true);
    }
}
