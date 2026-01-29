package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.service.UserBalanceService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-balance")
@RequiredArgsConstructor
public class UserBalanceController {
    private final UserBalanceService userBalanceService;

    @PostMapping("/add-balance")
    public HttpEntity<?> addBalance(@RequestBody UserBalanceReqDto dto) {
        ApiResponse response = userBalanceService.create(dto);
        return ResponseEntity.ok(response);

    }
     @GetMapping("/view-list/{userId}")
    public HttpEntity<?> addBalance(@PathVariable Long userId) {
        ApiResponse response = userBalanceService.getAll(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-balance-sta/{userId}")
    public HttpEntity<?> totalBalance(@PathVariable Long userId){
        ApiResponse response=userBalanceService.totalBalance(userId);
        return ResponseEntity.ok(response);
    }



}
