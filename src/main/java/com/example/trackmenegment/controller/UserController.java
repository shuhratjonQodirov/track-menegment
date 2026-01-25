package com.example.trackmenegment.controller;

import com.example.trackmenegment.config.UserPrincipal;
import com.example.trackmenegment.dto.req.UserRequestDto;
import com.example.trackmenegment.service.UserService;
import com.example.trackmenegment.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")

public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Foydalanuvchi autentifikatsiya qilinmagan");
        }
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        ApiResponse response = userService.getUser(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-driver")
    public HttpEntity<?> createDriver(@RequestBody UserRequestDto userRequest) {
        ApiResponse response = userService.createDriver(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-manager")
    public HttpEntity<?> createManager(@RequestBody UserRequestDto userRequest) {
        ApiResponse response = userService.createManager(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-economist")
    public HttpEntity<?> createEconomist(@RequestBody UserRequestDto userRequest) {
        ApiResponse response = userService.createEconomist(userRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getUser(@PathVariable Long id) {
        ApiResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-directors")
    public HttpEntity<?> getDirectors() {
        ApiResponse response = userService.getDirectors();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-driver")
    public HttpEntity<?> getDriverList() {
        ApiResponse response = userService.getListOfDriver();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-manager")
    public HttpEntity<?> getManagerList() {
        ApiResponse response = userService.getListOfManager();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-economist")
    public HttpEntity<?> getEconomistList() {
        ApiResponse response = userService.getListOfEconomist();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public HttpEntity<?> update(@RequestBody UserRequestDto dto, @PathVariable Long id) {
        ApiResponse response = userService.update(dto,id);
        return ResponseEntity.ok(response);

    }



}
