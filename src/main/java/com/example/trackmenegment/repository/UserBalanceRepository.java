package com.example.trackmenegment.repository;

import com.example.trackmenegment.dto.res.UserBalanceTotalResDto;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    List<UserBalance> findAllByTripAndDeletedFalse(Trip trip);

    List<UserBalance> findAllByUserAndDeletedFalse(User user);

    Optional<UserBalance> findByIdAndDeletedFalse(Long id);

    @Query(nativeQuery = true, value = "SELECT COALESCE(SUM(CASE WHEN u.payment_type IN ('SALARY', 'BONUS') THEN u.amount_usd ELSE 0 END), 0) AS totalSalary, COALESCE(SUM(CASE WHEN u.payment_type = 'PERSONAL' THEN u.amount_usd ELSE 0 END), 0) AS totalPersonal, COALESCE(SUM(CASE WHEN u.payment_type = 'ADVANCE' THEN u.amount_usd ELSE 0 END), 0) AS totalAdvance, COALESCE(SUM(CASE WHEN u.payment_type = 'PENALTY' THEN u.amount_usd ELSE 0 END), 0) AS totalPenalty, COALESCE(SUM(CASE WHEN u.payment_type IN ('SALARY', 'BONUS') AND u.is_paid = true THEN u.amount_usd ELSE 0 END), 0) AS totalPaid,COALESCE(SUM(CASE WHEN u.payment_type IN ('SALARY', 'BONUS') AND u.is_paid = false THEN u.amount_usd WHEN u.payment_type IN ('PERSONAL', 'ADVANCE', 'PENALTY') THEN -u.amount_usd ELSE 0 END), 0) AS nowBalance FROM user_balance AS u  WHERE u.user_id = :userId\n")
    Optional<UserBalanceTotalResDto> totalBalance(@Param(value = "userId") Long userId);


}
