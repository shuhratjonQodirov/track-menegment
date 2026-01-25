package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.TripReqDto;
import com.example.trackmenegment.service.trip.TripService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping("/create")
    public HttpEntity<?> createTrip(@RequestBody TripReqDto reqTripDto) {
        ApiResponse response = tripService.create(reqTripDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> getAllTrips() {
        ApiResponse response = tripService.getAllTrips();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Long id) {
        ApiResponse response = tripService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateTrip(@PathVariable Long id, @RequestBody TripReqDto tripReqDto) {
        ApiResponse response = tripService.updateTrip(id, tripReqDto);
        return ResponseEntity.ok(response);
    }
}
