package com.example.trackmenegment.model;


import com.example.trackmenegment.enums.UserRoles;
import com.example.trackmenegment.utils.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User extends AbsEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String passportNumber;

    @Column(unique = true, nullable = false)
    private String pnfl;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate brithDate;

    private int age;
    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles roles;

    private LocalDate hireDate;

    private LocalDate fireDate;

    @PrePersist
    protected void onCreate() {
        if (brithDate == null) age = 0;
        else age = Period.between(brithDate, LocalDate.now()).getYears();
    }
}

