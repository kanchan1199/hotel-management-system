package com.hotel.management.repository;

import com.hotel.management.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByGuestId(String guestId);
    Optional<Guest> findByEmail(String email);
    Optional<Guest> findByIdProofNumber(String idProofNumber);
}