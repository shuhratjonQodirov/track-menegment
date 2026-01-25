package com.example.trackmenegment.component;

import com.example.trackmenegment.enums.UserRoles;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Value("${spring.sql.init.mode}")
    private String mode;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            User user = User.builder()
                    .pnfl("51002006900025")
                    .roles(UserRoles.DIRECTOR)
                    .address("Oltiariq tuman")
                    .phoneNumber("+998905823433")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Shuhrat")
                    .age(25)
                    .brithDate(LocalDate.of(2000,2,10))
                    .passportNumber("AB5920931")
                    .build();
            userRepository.save(user);
        }
    }
}
