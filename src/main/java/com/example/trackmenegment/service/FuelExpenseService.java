package com.example.trackmenegment.service;

import com.example.trackmenegment.dto.req.FuelExpenseReqDto;
import com.example.trackmenegment.dto.res.FuelExpenseResDto;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.mapper.FuelExpenseMapper;
import com.example.trackmenegment.model.FuelExpense;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.repository.FuelExpenseRepository;
import com.example.trackmenegment.repository.TripRepository;
import com.example.trackmenegment.repository.TruckRepository;
import com.example.trackmenegment.repository.UserRepository;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.FuelExpenseValidator;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public interface FuelExpenseService {

    ApiResponse create(FuelExpenseReqDto dto);

    ApiResponse update(Long id, FuelExpenseReqDto dto);

    ApiResponse delete(Long id);

    ApiResponse getByTripId(Long tripId);
}
