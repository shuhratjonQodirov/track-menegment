package com.example.trackmenegment.controller;

import com.example.trackmenegment.service.DashboardService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public HttpEntity<?> getStatistic() {
        ApiResponse response = dashboardService.getStatistic();
        return ResponseEntity.ok(response);

    }
}
