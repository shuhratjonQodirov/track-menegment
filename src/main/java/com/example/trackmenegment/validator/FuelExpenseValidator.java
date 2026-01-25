package com.example.trackmenegment.validator;

import com.example.trackmenegment.dto.req.FuelExpenseReqDto;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.error.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class FuelExpenseValidator {
    public void validateDto(FuelExpenseReqDto fuelDto) {

        if (fuelDto.getTripId() == null || fuelDto.getTripId() <= 0) {
            throw new ByIdException("Trip ID bo'sh yoki noto'g'ri");
        }

        if (fuelDto.getTruckId() == null || fuelDto.getTruckId() <= 0) {
            throw new ByIdException("Truck ID bo'sh yoki noto'g'ri");
        }
        if (fuelDto.getLitres() == null || fuelDto.getLitres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Litres 0 dan katta bo'lishi kerak");
        }

        if (fuelDto.getPricePerLitres() == null || fuelDto.getPricePerLitres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Price per litre 0 dan katta bo'lishi kerak");
        }

        if (fuelDto.getLocalCurrency() == null) {
            throw new ByIdException("Valyuta tanlanishi kerak");
        }

        if (fuelDto.getAmountLocal() == null || fuelDto.getAmountLocal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Amount local 0 dan katta bo'lishi kerak");
        }

        if (fuelDto.getTotalAmountUsd() == null || fuelDto.getTotalAmountUsd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Total USD 0 dan katta bo'lishi kerak");
        }

        if (StringUtils.isBlank(fuelDto.getGasStationName())) {
            throw new ByIdException("Gas station name bo'sh bo'lishi mumkin emas");
        }

        if (StringUtils.isBlank(fuelDto.getCountry())) {
            throw new ByIdException("Country bo'sh bo'lishi mumkin emas");
        }

        if (fuelDto.getFuelDate() == null || fuelDto.getFuelDate().isAfter(LocalDate.now())) {
            throw new ByIdException("Fuel date noto'g'ri");
        }
    }
    public void validateForUpdate(FuelExpenseReqDto dto) {
        if (dto.getLitres() != null && dto.getLitres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ByIdException("Liters must be positive");
        }
        if (dto.getPricePerLitres() != null &&
                dto.getPricePerLitres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price per liter must be positive");
        }
    }

}
