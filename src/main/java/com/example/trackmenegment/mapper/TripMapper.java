package com.example.trackmenegment.mapper;

import com.example.trackmenegment.dto.req.TripReqDto;
import com.example.trackmenegment.dto.res.TripResDto;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.model.User;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    public Trip toEntity(TripReqDto reqTripDto, User user, Truck truck){
        return Trip.builder()
                .user(user)
                .truck(truck)
                .tripStatus(reqTripDto.getTripStatus()).
                routeFrom(reqTripDto.getRouteFrom())
                .routeTo(reqTripDto.getRouteTo()).
                incomeOutUsd(reqTripDto.getIncomeOutUsd())
                .incomeBackUsd(reqTripDto.getIncomeBackUsd())
                .driverIncomeUsd(reqTripDto.getDriverIncomeUsd())
                .driverIncomePaid(reqTripDto.getDriverIncomePaid())
                .startedDate(reqTripDto.getStartedDate())
                .finishedDate(reqTripDto.getFinishedDate())
                .build();
    }


}
