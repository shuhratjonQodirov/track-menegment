package com.example.trackmenegment.controller;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.service.UserBalanceService;
import com.example.trackmenegment.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-balance")
@RequiredArgsConstructor
public class UserBalanceController {
    private final UserBalanceService userBalanceService;

    @PostMapping("/add-balance")
    public HttpEntity<?> addBalance(@RequestBody @Valid UserBalanceReqDto dto) {
        ApiResponse response = userBalanceService.create(dto);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/view-list/{userId}")
    public HttpEntity<?> addBalance(@PathVariable Long userId) {
        ApiResponse response = userBalanceService.getAll(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-balance-sta/{userId}")
    public HttpEntity<?> totalBalance(@PathVariable Long userId) {
        ApiResponse response = userBalanceService.totalBalance(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{userBalanceId}")
    public HttpEntity<?> update(@RequestBody @Valid UserBalanceReqDto dto, @PathVariable Long userBalanceId) {
        ApiResponse response = userBalanceService.update(userBalanceId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get-report-pdf/{userId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getPdfUserBalance(@PathVariable Long userId) {
        byte[] response = userBalanceService.getPdfUserBalance(userId);

        String fileName = "Balans_Hisoboti_ID_" + userId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(response.length)
                .body(response);
    }
}
