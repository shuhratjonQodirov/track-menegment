package com.example.trackmenegment.repository;

import com.example.trackmenegment.enums.TruckStatus;
import com.example.trackmenegment.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
    Optional<Truck> findByTruckNumberAndDeletedFalse(String truckNumber);
    Optional<Truck> findByIdAndDeletedFalse(Long id);

    boolean existsByTruckNumberAndDeletedFalse(String truckNumber);
    List<Truck> findAllByDeletedFalse();

    Long countByTruckStatusAndDeletedFalse(TruckStatus status);

}
