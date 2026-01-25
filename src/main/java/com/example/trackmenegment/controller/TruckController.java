package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.TruckReqDto;
import com.example.trackmenegment.service.TruckService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/truck")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService truckService;

    @PostMapping("/create")
    public HttpEntity<?> createTruck(@RequestBody TruckReqDto truckReqDto) {
        ApiResponse response = truckService.create(truckReqDto);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/list-truck")
    public HttpEntity<?> getListOfTruck() {
        ApiResponse response = truckService.getAllList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneById(@PathVariable Long id) {
        ApiResponse response = truckService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public HttpEntity<?> update(@PathVariable Long id, @RequestBody TruckReqDto dto) {
        ApiResponse response = truckService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteTruck(@PathVariable Long id) {
        ApiResponse response = truckService.delete(id);
        return ResponseEntity.ok(response);
    }

}
