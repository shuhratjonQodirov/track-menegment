package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.TripExpense;
import com.example.trackmenegment.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    List<UserBalance    > findAllByTripAndDeletedFalse(Trip trip);
}
