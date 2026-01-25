package com.example.trackmenegment.dto.req;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public class UserRequestDto {

        private String fullName;

        private String phoneNumber;

        private String passportNumber;

        private String pnfl;

        private String password;

        private String address;

        private LocalDate brithDate;

        private LocalDate hireDate;
    }
