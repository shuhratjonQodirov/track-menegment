package com.example.trackmenegment.repository;

import com.example.trackmenegment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPnflAndDeletedFalse(String pnfl);

    Optional<User> findByIdAndDeletedFalse(Long id);

    boolean existsByPnflAndDeletedFalse(String pnfl);

    List<User> findAllByDeletedFalse();
}
