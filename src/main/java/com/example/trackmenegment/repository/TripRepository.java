package com.example.trackmenegment.repository;

import com.example.trackmenegment.dto.res.TripResDto;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.Truck;
import com.example.trackmenegment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {


    List<Trip> findAllByDeletedFalse();

    @Query(nativeQuery = true, value = "SELECT t.id AS id, tr.truck_number AS truckNumber,u.full_name AS fullName, t.route_from AS routeFrom,  t.route_to AS routeTo,t.income_out_usd AS incomeOutUsd, t.income_back_usd AS incomeBackUsd,t.trip_status as tripStatus,t.trip_duration AS tripDuration FROM trip t JOIN truck tr ON t.truck_id = tr.id JOIN users u ON t.user_id = u.id")
    List<TripResDto> findAllTripDto();

//    @Query(nativeQuery = true,value = "SELECT t.* FROM trip t JOIN truck tr ON tr.id = t.truck_id JOIN users u ON u.id = t.user_id WHERE t.id = :tripId")
    Optional<Trip> findByIdAndDeletedFalse(@Param("tripId")Long id);


    Optional<Trip> findByTruckAndDeletedFalse(Truck truck);
    List<Trip> findAllByUserAndDeletedFalse(User user);
}
