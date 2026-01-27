package com.example.trackmenegment.validator;

import com.example.trackmenegment.dto.req.TruckExpenseReq;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.error.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TruckExpenseValidator {

    public void validateForCreate(TruckExpenseReq expenseReq) {
        if (expenseReq.getTruckId() == null || expenseReq.getTruckId() <= 0) {
            throw new ValidationException("Truck Id bo'sh yoki natog'ri");
        }
        if (expenseReq.getTripId() == null || expenseReq.getTripId() <= 0) {
            throw new ValidationException("Trip Id bo'sh yoki natog'ri");

        }
        if (StringUtils.isBlank(expenseReq.getTruckExpenseType())) {
            throw new ValidationException("Truck expense type cannot be empty");
        }
        if (StringUtils.isBlank(expenseReq.getLocalCurrency())) {
            throw new ValidationException("Local currency  type cannot be empty");
        }
        if (StringUtils.isBlank(expenseReq.getDescription())) {
            throw new ValidationException("Description  cannot be empty");
        }
        if (expenseReq.getAmountLocal() == null || expenseReq.getAmountLocal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount local 0 dan katta bo'lishi kerak");
        }
        if (expenseReq.getAmountUsd() == null || expenseReq.getAmountUsd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount local 0 dan katta bo'lishi kerak");
        }
        if (expenseReq.getExpenseDate() == null || expenseReq.getExpenseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Expense date noto'g'ri");
        }
    }

    public void validateForUpdate(TruckExpenseReq truckExpenseReq) {

        if (truckExpenseReq.getTruckId() == null || truckExpenseReq.getTruckId() == 0) {
            throw new ValidationException("Truck id cannot be null or 0");
        }
        if (truckExpenseReq.getTruckExpenseType().isBlank()) {
            throw new ValidationException("Expense type cannot be empty");
        }
        if (truckExpenseReq.getLocalCurrency().isBlank()) {
            throw new ValidationException("Local currency type cannot be empty");
        }
        if (truckExpenseReq.getDescription().isBlank()) {
            throw new ValidationException("Description type cannot be empty");
        }
        if (truckExpenseReq.getAmountLocal() == null || truckExpenseReq.getAmountLocal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount usd 0 dan katta bo'lishi kerak");
        }
        if (truckExpenseReq.getAmountUsd() == null || truckExpenseReq.getAmountUsd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount usd 0 dan katta bo'lishi kerak");
        }
        if (truckExpenseReq.getExpenseDate() == null || truckExpenseReq.getExpenseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Expense Date cannot be future");
        }


    }
}
