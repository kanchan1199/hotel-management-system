package com.hotel.management.repository;

import com.hotel.management.model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByReservationId(Long reservationId);
    List<Charge> findByReservationConfirmationNumber(String confirmationNumber);
}