package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.FuelExpenseReqDto;
import com.example.trackmenegment.service.FuelExpenseService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fuel")
@RequiredArgsConstructor
public class FuelExpenseController {
    private final FuelExpenseService fuelExpenseService;


    @PostMapping("/create")
    public HttpEntity<?> createFuel(@RequestBody FuelExpenseReqDto fuelDto) {
        ApiResponse response = fuelExpenseService.create(fuelDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public HttpEntity<?> updateFuel(@PathVariable Long id, @RequestBody FuelExpenseReqDto dto) {
        ApiResponse response = fuelExpenseService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResponse response = fuelExpenseService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/get-by-tripId/{tripId}")
    public HttpEntity<?> getByTripId(@PathVariable Long tripId) {
        ApiResponse response = fuelExpenseService.getByTripId(tripId);
        return ResponseEntity.ok(response);
    }

}
