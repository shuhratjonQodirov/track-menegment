package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.dto.req.TruckExpenseReq;
import com.example.trackmenegment.enums.Currency;
import com.example.trackmenegment.enums.TruckExpenseType;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.mapper.TruckExpenseMapper;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.model.TruckExpense;
import com.example.trackmenegment.repository.TripRepository;
import com.example.trackmenegment.repository.TruckExpenseRepository;
import com.example.trackmenegment.repository.TruckRepository;
import com.example.trackmenegment.service.TruckExpenseService;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.TruckExpenseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TruckExpenseServiceImpl implements TruckExpenseService {
    private final TruckExpenseRepository truckExpenseRepository;
    private final TruckExpenseValidator truckExpenseValidator;
    private final TruckExpenseMapper truckExpenseMapper;
    private final TripRepository tripRepository;
    private final TruckRepository truckRepository;

    @Override
    public ApiResponse create(TruckExpenseReq expenseReq) {

        truckExpenseValidator.validateForCreate(expenseReq);
        Trip trip = tripRepository.findByIdAndDeletedFalse(expenseReq.getTripId()).orElseThrow(() -> new ByIdException("Trip not found"));
        Truck truck = truckRepository.findByIdAndDeletedFalse(expenseReq.getTruckId()).orElseThrow(() -> new ByIdException("Truck not found"));

        TruckExpense expense = truckExpenseMapper.toEntity(expenseReq, truck, trip);
        truckExpenseRepository.save(expense);

        return new ApiResponse("Truck expense saved", true);
    }

    @Override
    public ApiResponse delete(Long id) {
        TruckExpense expense = truckExpenseRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Truck expense not found"));
        expense.setDeleted(true);
        truckExpenseRepository.save(expense);
        return new ApiResponse("truck Expense successfully deleted ", true);
    }

    @Override
    public ApiResponse update(Long truckExpense, TruckExpenseReq dto) {

        truckExpenseValidator.validateForUpdate(dto);

        TruckExpense expense = truckExpenseRepository.findByIdAndDeletedFalse(truckExpense).orElseThrow(() -> new ByIdException("Truck Expense not found"));

        Truck truck = truckRepository.findByIdAndDeletedFalse(dto.getTruckId()).orElseThrow(() -> new ByIdException("Truck not found"));
        expense.setTruck(truck);

        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId()).orElseThrow(() -> new ByIdException("Trip not found"));
            expense.setTrip(trip);
        }
        expense.setTruckExpenseType(TruckExpenseType.valueOf(dto.getTruckExpenseType()));
        expense.setAmountUsd(dto.getAmountUsd());
        expense.setAmountLocal(dto.getAmountLocal());
        expense.setLocalCurrency(Currency.valueOf(dto.getLocalCurrency()));
        expense.setDescription(dto.getDescription());
        expense.setExpenseDate(dto.getExpenseDate());

        truckExpenseRepository.save(expense);

        return new ApiResponse("Muvaffaqiyatli yangilandi", true);
    }


}
