package com.example.trackmenegment.service.trip;

import com.example.trackmenegment.dto.req.TripReqDto;
import com.example.trackmenegment.dto.res.*;
import com.example.trackmenegment.enums.PaymentType;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.model.*;
import com.example.trackmenegment.repository.*;
import com.example.trackmenegment.utils.ApiResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface TripService {

    ApiResponse create(TripReqDto reqTripDto);

    ApiResponse getAllTrips() ;


    ApiResponse getById(Long id);

    ApiResponse updateTrip(Long id, TripReqDto tripReqDto);

    ApiResponse getTripByUserId(Long userId);
}
