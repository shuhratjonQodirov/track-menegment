package com.example.trackmenegment.service.impl;

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
import com.example.trackmenegment.service.FuelExpenseService;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.FuelExpenseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelExpenseImpl implements FuelExpenseService {
    private final FuelExpenseRepository fuelExpenseRepository;
    private final TripRepository tripRepository;
    private final TruckRepository truckRepository;
    private final FuelExpenseValidator fuelExpenseValidator;
    private final FuelExpenseMapper mapper;

    @Override
    public ApiResponse create(FuelExpenseReqDto fuelDto) {

        fuelExpenseValidator.validateDto(fuelDto);

        Trip trip = tripRepository.findByIdAndDeletedFalse(fuelDto.getTripId()).orElseThrow(() -> new ByIdException("Trip not found"));
        Truck truck = truckRepository.findByIdAndDeletedFalse(fuelDto.getTruckId()).orElseThrow(() -> new ByIdException("Truck not found"));

        FuelExpense fuel = mapper.toEntity(fuelDto, truck, trip);

        fuelExpenseRepository.save(fuel);

        return new ApiResponse("fuel expense saved successfully", true);
    }

    @Override

    public ApiResponse update(Long id, FuelExpenseReqDto dto) {
        return null;
    }
    @Override

    public ApiResponse delete(Long id) {
        FuelExpense fuelExpense = fuelExpenseRepository.findById(id).orElseThrow(() -> new ByIdException("Fuel expense not found"));
        fuelExpense.setDeleted(true);
        fuelExpenseRepository.save(fuelExpense);
        return new ApiResponse("Fuel expense Successfully deleted", true);

    }
    @Override

    public ApiResponse getByTripId(Long tripId) {
        List<FuelExpenseResDto> list = fuelExpenseRepository.findAllByTripIdAndDeletedFalse(tripId).stream()
                .map(mapper::toDto)
                .toList();
        return new ApiResponse("list of fuel expenses", true, list);
    }
}
