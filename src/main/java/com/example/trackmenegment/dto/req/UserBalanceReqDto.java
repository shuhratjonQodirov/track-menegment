package com.example.trackmenegment.dto.req;

import com.example.trackmenegment.enums.PaymentType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBalanceReqDto {

    @NotNull(message = "User ID bo'sh bo'lmasligi kerak")
    private Long userId;

    private Long tripId;

    @NotNull(message = "Summa ko'rsatilishi shart")
    @DecimalMin(value = "0.0", inclusive = false, message = "Summa 0 dan katta bo'lishi kerak")
    private BigDecimal amountUsd;

    @NotNull(message = "To'lov turi ko'rsatilishi shart")
    private PaymentType paymentType;

    @NotBlank(message = "Izoh bo'sh bo'lmasligi kerak")
    private String description;

    @NotNull(message = "Operatsiya sanasi ko'rsatilmadi")
    @PastOrPresent(message = "Sana kelajakdagi vaqt bo'lishi mumkin emas")
    private LocalDate operationDate;

    @NotNull(message = "To'lov holati (isPaid) ko'rsatilishi shart")
    private Boolean isPaid;
}
