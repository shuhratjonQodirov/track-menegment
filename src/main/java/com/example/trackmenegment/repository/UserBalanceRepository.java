package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.TripExpense;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    List<UserBalance  > findAllByTripAndDeletedFalse(Trip trip);

    List<UserBalance> findAllByUserAndDeletedFalse(User user);

    Optional<UserBalance> findByIdAndDeletedFalse(Long id);


}
