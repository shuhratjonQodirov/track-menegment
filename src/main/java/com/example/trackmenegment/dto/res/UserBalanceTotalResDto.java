package com.example.trackmenegment.dto.res;

import java.math.BigDecimal;

public interface UserBalanceTotalResDto {
    BigDecimal getTotalSalary();
    BigDecimal getTotalPersonal();
    BigDecimal getTotalAdvance();
    BigDecimal getTotalPenalty();
    BigDecimal getNowBalance();
    BigDecimal getTotalPaid();
}
