package com.example.trackmenegment.service.trip;

import com.example.trackmenegment.dto.req.TripReqDto;
import com.example.trackmenegment.dto.res.*;
import com.example.trackmenegment.enums.PaymentType;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.mapper.TripMapper;
import com.example.trackmenegment.model.*;
import com.example.trackmenegment.repository.*;
import com.example.trackmenegment.service.UserBalanceService;
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

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final UserRepository userRepository;
    private final TruckRepository truckRepository;
    private final TripRepository tripRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final TruckExpenseRepository truckExpenseRepository;
    private final TripExpenseRepository tripExpenseRepository;
    private final FuelExpenseRepository fuelExpenseRepository;
    private final TripMapper tripMapper;
    private final UserBalanceService userBalanceService;

    @Override
    public ApiResponse create(TripReqDto reqTripDto) {
        User user = userRepository.findByIdAndDeletedFalse(
                reqTripDto.getDriverId()).orElseThrow(() -> new ByIdException("User not found"));

        Truck truck = truckRepository.findByIdAndDeletedFalse(reqTripDto.getTruckId()).orElseThrow(() -> new ByIdException("truck not found"));

        Trip trip = tripMapper.toEntity(reqTripDto, user, truck);

        Trip save = tripRepository.save(trip);
         userBalanceService.createDriverBalance(
                 user,
                 save
                 , reqTripDto.getDriverIncomeUsd(),
                 reqTripDto.getDriverIncomePaid());

        return new ApiResponse("yangi sayoxat saqlandi", true);
    }

    @Override
    public ApiResponse getAllTrips() {
        List<TripResDto> tripList = tripRepository.findAllTripDto();
        return new ApiResponse("barcha reyslar ro'yxati", true, tripList);
    }

    @Override
    public ApiResponse getById(Long id) {
        Trip trip = tripRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("trip not found"));

        TripResById res = new TripResById();
        res.setId(trip.getId());

        Truck truck = trip.getTruck();
        res.setTruckId(truck.getId());
        res.setTruckNumber(truck.getTruckNumber());

        User driver = trip.getUser();
        res.setDriverId(driver.getId());
        res.setDriverFullName(driver.getFullName());

        res.setRouteTo(trip.getRouteTo());
        res.setRouteFrom(trip.getRouteFrom());

        res.setIncomeOutUsd(trip.getIncomeOutUsd());
        res.setIncomeBackUsd(trip.getIncomeBackUsd());
        res.setTotalIncomeUsd(trip.getIncomeOutUsd().add(trip.getIncomeBackUsd()));

        List<TripExpense> tripExpenseList = tripExpenseRepository.findAllByTripAndDeletedFalse(trip);
        BigDecimal totalTripExpensesUsd = calculateTripExpense(tripExpenseList); ///usd
        List<TripExpenseResDto> tripExpenseResDtoList = convertToTripExpenseDto(tripExpenseList);
        res.setTotalTripExpensesUsd(totalTripExpensesUsd);
        res.setTripExpenseResDto(tripExpenseResDtoList);

        List<TruckExpense> truckExpenseList = truckExpenseRepository.findAllByTripAndDeletedFalse(trip);
        BigDecimal totalTruckExpense = calculateTotalTruckExpense(truckExpenseList); ///usd
        List<TruckExpenseResDto> truckExpenseResDtoList = convertToTruckExpenseDto(truckExpenseList);
        res.setTruckExpensesTotalUsd(totalTruckExpense);
        res.setTruckExpenses(truckExpenseResDtoList);

        List<FuelExpense> fuelExpenseList = fuelExpenseRepository.findAllByTripAndDeletedFalse(trip);
        BigDecimal totalLiters = calculateTotalLiters(fuelExpenseList);
        BigDecimal totalFuelCostUsd = calculateTotalCostUsd(fuelExpenseList); /// usd
        List<FuelExpenseResDto> fuelExpenseResDtoList = convertToFuelExpenseResDto(fuelExpenseList);
        res.setTotalLiters(totalLiters);
        res.setTotalFuelCostUsd(totalFuelCostUsd);
        res.setFuelEntries(fuelExpenseResDtoList);

        res.setDriverIncomeUsd(trip.getDriverIncomeUsd());

        List<UserBalance> userBalanceList = userBalanceRepository.findAllByTripAndDeletedFalse(trip);
        BigDecimal personalExpenseDrive = calculatePersonalExpenseByTrip(userBalanceList);
        List<UserBalanceResponse> userBalanceResponseList = convertToUserBalanceResDto(userBalanceList);
        res.setPersonalExpenseDriver(personalExpenseDrive);
        res.setUserBalanceResponseList(userBalanceResponseList);

        BigDecimal totalExpense = calculateTotalExpenseUsd(
                totalTripExpensesUsd,
                totalTruckExpense,
                totalFuelCostUsd,
                trip.getDriverIncomeUsd(), personalExpenseDrive);
        res.setTotalExpense(totalExpense);

        BigDecimal profit = calculateProfit(res.getTotalIncomeUsd(), totalExpense);

        res.setProfitUsd(profit);
        res.setTripStatus(trip.getTripStatus());
        res.setStartedDate(trip.getStartedDate());
        res.setFinishedDate(trip.getFinishedDate());
        res.setTripDuration(calculateTripDuration(trip.getStartedDate(), trip.getFinishedDate()));

        return new ApiResponse("", true, res);
    }

    @Override
    public ApiResponse updateTrip(Long id, TripReqDto tripReqDto) {
        Trip trip = tripRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ByIdException("Trip not found"));

        if (tripReqDto.getTruckId() != 0) {
            Truck truck = truckRepository.findByIdAndDeletedFalse(tripReqDto.getTruckId()).orElseThrow(() -> new ByIdException("Truck not found"));
            trip.setTruck(truck);
        }
        if (tripReqDto.getDriverId() != 0) {
            User user = userRepository.findByIdAndDeletedFalse(tripReqDto.getDriverId()).orElseThrow(() -> new ByIdException("Driver not found"));
            trip.setUser(user);
        }

        if (tripReqDto.getStartedDate() != null && tripReqDto.getFinishedDate() != null) {
            if (tripReqDto.getFinishedDate().isBefore(tripReqDto.getStartedDate())) {
                throw new ByIdException("Tugash sanasi boshlanish sanasidan oldin bo'lishi mumkin emas");
            }
            trip.setStartedDate(tripReqDto.getStartedDate());
            trip.setFinishedDate(tripReqDto.getFinishedDate());
        }

        if (tripReqDto.getTripDuration() >= 0) {
            trip.setTripDuration(tripReqDto.getTripDuration());
        }

        if (tripReqDto.getIncomeOutUsd() != BigDecimal.ZERO) trip.setIncomeOutUsd(tripReqDto.getIncomeOutUsd());
        if (tripReqDto.getIncomeBackUsd() != BigDecimal.ZERO) trip.setIncomeBackUsd(tripReqDto.getIncomeBackUsd());
        if (tripReqDto.getDriverIncomeUsd() != BigDecimal.ZERO)
            trip.setDriverIncomeUsd(tripReqDto.getDriverIncomeUsd());
        if (tripReqDto.getDriverIncomePaid() != null) trip.setDriverIncomePaid(tripReqDto.getDriverIncomePaid());

        if (tripReqDto.getTripStatus() != null) trip.setTripStatus(tripReqDto.getTripStatus());

        if (StringUtils.isNotBlank(tripReqDto.getRouteFrom())) trip.setRouteFrom(tripReqDto.getRouteFrom());
        if (StringUtils.isNotBlank(tripReqDto.getRouteTo())) trip.setRouteTo(tripReqDto.getRouteTo());
        tripRepository.save(trip);

        return new ApiResponse("Trip successfully updated", true);
    }


    public int calculateTripDuration(LocalDate startedDate, LocalDate finishedDate) {
        if (startedDate == null) {
            return 0;
        }

        LocalDate endDate = (finishedDate != null)
                ? finishedDate
                : LocalDate.now();

        return (int) ChronoUnit.DAYS.between(startedDate, endDate) + 1;
    }

    private BigDecimal calculateProfit(BigDecimal totalIncomeUsd, BigDecimal totalExpense) {
        return totalIncomeUsd.subtract(totalExpense);
    }

    private BigDecimal calculateTotalExpenseUsd(BigDecimal totalTripExpensesUsd, BigDecimal totalTruckExpense,
                                                BigDecimal totalFuelCostUsd, BigDecimal driverIncomeUsd, BigDecimal personalExpenseDrive) {
        return totalTripExpensesUsd.add(totalTruckExpense)
                .add(totalFuelCostUsd)
                .add(driverIncomeUsd);
    }

    private List<UserBalanceResponse> convertToUserBalanceResDto(List<UserBalance> userBalanceList) {
        return userBalanceList.stream()
                .map(driverBalance ->
                        UserBalanceResponse
                                .builder()
                                .id(driverBalance.getId())
                                .tripId(driverBalance.getTrip().getId())
                                .userId(driverBalance.getUser().getId())
                                .paymentType(driverBalance.getPaymentType())
                                .operationDate(driverBalance.getOperationDate())
                                .description(driverBalance.getDescription())
                                .amountUsd(driverBalance.getAmountUsd())
                                .build()).collect(Collectors.toList());
    }

    private BigDecimal calculatePersonalExpenseByTrip(List<UserBalance> userBalanceList) {
        return userBalanceList.stream().
                filter(driverBalance ->
                        driverBalance.getPaymentType() == PaymentType.PERSONAL || driverBalance.getPaymentType() == PaymentType.ADVANCE)
                .map(UserBalance::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<FuelExpenseResDto> convertToFuelExpenseResDto(List<FuelExpense> fuelExpenseList) {
        return fuelExpenseList
                .stream()
                .map(fuelExpense ->
                        FuelExpenseResDto
                                .builder()
                                .id(fuelExpense.getId())
                                .fuelDate(fuelExpense.getFuelDate())
                                .location(fuelExpense.getCountry())
                                .liters(fuelExpense.getLiters())
                                .country(fuelExpense.getCountry())
                                .pricePerLiterUsd(fuelExpense.getPricePerLiter())
                                .totalAmountUsd(fuelExpense.getTotalAmountUsd())
                                .gasStationName(fuelExpense.getGasStationName())
                                .localCurrency(fuelExpense.getLocalCurrency().toString())
                                .amountLocal(fuelExpense.getAmountLocal())
                                .localCurrency(fuelExpense.getLocalCurrency().toString())
                                .build()

                ).collect(Collectors.toList());
    }

    private BigDecimal calculateTotalCostUsd(List<FuelExpense> fuelExpenseList) {
        return fuelExpenseList
                .stream()
                .map(FuelExpense::getTotalAmountUsd)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalLiters(List<FuelExpense> fuelExpenseList) {
        return fuelExpenseList
                .stream()
                .map(FuelExpense::getLiters)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TripExpenseResDto> convertToTripExpenseDto(List<TripExpense> tripExpenseList) {
        return tripExpenseList
                .stream()
                .map(tripExpense ->
                        TripExpenseResDto.builder()
                                .id(tripExpense.getId())
                                .type(tripExpense.getType())
                                .amountLocal(tripExpense.getAmountLocal())
                                .localCurrency(tripExpense.getLocalCurrency())
                                .amountUsd(tripExpense.getAmountUsd())
                                .description(tripExpense.getDescription())
                                .expenseDate(tripExpense.getExpenseDate())
                                .build()).collect(Collectors.toList());
    }

    private BigDecimal calculateTripExpense(List<TripExpense> tripExpenseList) {
        return tripExpenseList.stream()
                .map(TripExpense::getAmountUsd)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TruckExpenseResDto> convertToTruckExpenseDto(List<TruckExpense> truckExpenseList) {
        return truckExpenseList.stream().map(
                truckExpense ->
                        TruckExpenseResDto.builder()
                                .id(truckExpense.getId())
                                .truckId(truckExpense.getTruck().getId())
                                .tripId(truckExpense.getTrip().getId())
                                .truckExpenseType(truckExpense.getTruckExpenseType())
                                .localCurrency(truckExpense.getLocalCurrency().toString())
                                .amountLocal(truckExpense.getAmountLocal())
                                .amountUsd(truckExpense.getAmountUsd())
                                .description(truckExpense.getDescription())
                                .expenseDate(truckExpense.getExpenseDate())
                                .build()
        ).collect(Collectors.toList());

    }

    private BigDecimal calculateTotalTruckExpense(List<TruckExpense> truckExpenseList) {
        return truckExpenseList
                .stream()
                .filter(Objects::nonNull)
                .map(TruckExpense::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
