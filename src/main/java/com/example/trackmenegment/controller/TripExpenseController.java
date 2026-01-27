package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.TripExpenseReqDto;
import com.example.trackmenegment.service.TripExpenseService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trip-expense")
public class TripExpenseController {

    private final TripExpenseService tripExpenseService;


    @PostMapping("/create")
    public HttpEntity<?> createExpense(@RequestBody TripExpenseReqDto dto) {
        ApiResponse response = tripExpenseService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-trip/{tripId}")
    public HttpEntity<?> getByTripId(@PathVariable Long tripId) {
        ApiResponse response = tripExpenseService.getByTripId(tripId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateTripExpense(@PathVariable Long id, @RequestBody TripExpenseReqDto dto) {
        ApiResponse response = tripExpenseService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteExpense(@PathVariable Long id) {
        ApiResponse response = tripExpenseService.delete(id);
        return ResponseEntity.ok(response);
    }


}
