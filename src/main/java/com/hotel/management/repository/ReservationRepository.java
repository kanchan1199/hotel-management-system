package com.hotel.management.repository;

import com.hotel.management.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByConfirmationNumber(String confirmationNumber);
    List<Reservation> findByGuestId(Long guestId);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    
    @Query("SELECT r FROM Reservation r WHERE r.guest.name LIKE %:guestName%")
    List<Reservation> findByGuestName(@Param("guestName") String guestName);
    
    @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :checkInDate AND r.checkOutDate <= :checkOutDate")
    List<Reservation> findByDateRange(@Param("checkInDate") LocalDate checkInDate, 
                                      @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.room.id = :roomId AND " +
           "r.status IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "((r.checkInDate <= :checkOutDate AND r.checkOutDate > :checkInDate))")
    boolean isRoomBooked(@Param("roomId") Long roomId, 
                        @Param("checkInDate") LocalDate checkInDate, 
                        @Param("checkOutDate") LocalDate checkOutDate);
}