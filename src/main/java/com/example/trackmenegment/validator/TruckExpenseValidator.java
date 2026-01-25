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
}
