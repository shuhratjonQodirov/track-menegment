package com.example.trackmenegment.model;

import com.example.trackmenegment.enums.TruckStatus;
import com.example.trackmenegment.utils.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Truck extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String truckNumber;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int manufactureYear;

    private BigDecimal buyPrice;

    private LocalDate buyDate;

    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;
}
