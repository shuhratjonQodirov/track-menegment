package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.TruckExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TruckExpenseRepository extends JpaRepository<TruckExpense, Long> {

    List<TruckExpense> findAllByTripAndDeletedFalse(Trip trip);

    Optional<TruckExpense> findByIdAndDeletedFalse(Long id);
}
