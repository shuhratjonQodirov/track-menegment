package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.TripExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripExpenseRepository extends JpaRepository<TripExpense, Long> {

    List<TripExpense> findAllByTripAndDeletedFalse(Trip trip);
    Optional<TripExpense> findByIdAndDeletedFalse(Long id);
    List<TripExpense> findByExpenseDateAndDeletedFalse(LocalDate date);

    List<TripExpense> findByExpenseDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);
}
