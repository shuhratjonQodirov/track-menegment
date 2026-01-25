package com.example.trackmenegment.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;

    private String phoneNumber;

    private String passportNumber;

    private String pnfl;
    private String address;
    private int age;
    private LocalDate brithDate;
    private String role;
    private LocalDate hireDate;
    private LocalDate fireDate;
}
