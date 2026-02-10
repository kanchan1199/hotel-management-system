package com.hotel.management.repository;

import com.hotel.management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByReservationId(Long reservationId);
    List<Payment> findByReservationConfirmationNumber(String confirmationNumber);
}