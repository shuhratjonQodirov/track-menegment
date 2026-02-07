package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.TruckExpenseReq;
import com.example.trackmenegment.service.TruckExpenseService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/truck-expense")
public class TruckExpenseController {
    private final TruckExpenseService truckExpenseService;


    @PostMapping("/create")
    public HttpEntity<?> create(@RequestBody TruckExpenseReq expenseReq) {
        ApiResponse response = truckExpenseService.create(expenseReq);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResponse response = truckExpenseService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{truckExpense}")
    public HttpEntity<?> update(@PathVariable Long truckExpense, @RequestBody TruckExpenseReq truckExpenseReq) {
        ApiResponse response = truckExpenseService.update(truckExpense, truckExpenseReq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/{truckId}")
    public HttpEntity<?> getTruckExpense(@PathVariable Long truckId) {
        ApiResponse response = truckExpenseService.getTruckExpense(truckId);
        return ResponseEntity.ok(response);
    }
}