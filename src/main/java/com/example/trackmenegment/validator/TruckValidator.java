package com.example.trackmenegment.validator;

import com.example.trackmenegment.dto.req.TruckReqDto;
import com.example.trackmenegment.error.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TruckValidator {

    public void validateForCreate(TruckReqDto truckReqDto){
        if (truckReqDto == null) {
            throw new ValidationException("Request data cannot be null");
        }
        if (StringUtils.isBlank(truckReqDto.getTruckNumber())) {
            throw new ValidationException("truck number cannot be empty");
        }

        if (StringUtils.isBlank(truckReqDto.getModelName())) {
            throw new ValidationException(" Truck model cannot be empty");
        }

        if (truckReqDto.getBuyDate() != null && truckReqDto.getBuyDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Buy date cannot be in the future");
        }

        if (truckReqDto.getBuyPrice() == null) {
            throw new ValidationException("buy price cannot be empty");
        }
        if (truckReqDto.getTruckStatus() == null) {
            throw new ValidationException("Truck status ko‘rsatilishi kerak");
        }
        if (truckReqDto.getManufactureYear() > LocalDate.now().getYear()) {
            throw new ValidationException("Ishlab chiqarilgan yil kelajakda bo‘la olmaydi");
        }
    }
    public void validateForUpdate(TruckReqDto dto) {
        if (dto.getManufactureYear() != 0 &&
                (dto.getManufactureYear() < 1900 || dto.getManufactureYear() > LocalDate.now().getYear())) {
            throw new ValidationException("Manufacture year invalid");
        }

        if (dto.getBuyPrice() != null && dto.getBuyPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Buy price must be positive");
        }

        if (dto.getBuyDate() != null && dto.getBuyDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Buy date cannot be in the future");
        }
    }

}
