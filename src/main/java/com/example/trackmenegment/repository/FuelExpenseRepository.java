package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.FuelExpense;
import com.example.trackmenegment.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuelExpenseRepository extends JpaRepository<FuelExpense, Long> {
    List<FuelExpense> findAllByTripAndDeletedFalse(Trip trip);

    List<FuelExpense> findAllByTripIdAndDeletedFalse(Long trip_id);

    List<FuelExpense> findByFuelDateAndDeletedFalse(LocalDate date);

    List<FuelExpense> findByFuelDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);

}
