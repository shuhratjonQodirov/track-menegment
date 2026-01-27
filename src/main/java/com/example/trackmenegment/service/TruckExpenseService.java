package com.example.trackmenegment.service;

import com.example.trackmenegment.dto.req.TruckExpenseReq;
import com.example.trackmenegment.utils.ApiResponse;

public interface TruckExpenseService {
    ApiResponse create(TruckExpenseReq expenseReq);

    ApiResponse delete(Long id);

    ApiResponse update(Long truckExpense, TruckExpenseReq truckExpenseReq);
}
