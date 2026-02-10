package com.hotel.management.repository;

import com.hotel.management.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
    Optional<Cancellation> findByCancellationId(String cancellationId);
    Optional<Cancellation> findByReservationConfirmationNumber(String confirmationNumber);
}