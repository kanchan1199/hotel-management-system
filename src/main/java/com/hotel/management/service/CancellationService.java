package com.hotel.management.service;

import com.hotel.management.exception.BusinessException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Cancellation;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.CancellationRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CancellationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private CancellationRepository cancellationRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Transactional
    public Map<String, Object> cancelReservation(String confirmationNumber, String reason) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reservation not found with confirmation number: " + confirmationNumber));
        
        // Cannot cancel if already checked in
        if (reservation.getStatus() == Reservation.ReservationStatus.CHECKED_IN) {
            throw new BusinessException("INVALID_STATUS", 
                "Cannot cancel reservation. Guest is already checked in. Please process check-out instead.");
        }
        
        // Cannot cancel if already cancelled
        if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
            throw new BusinessException("ALREADY_CANCELLED", 
                "This reservation has already been cancelled");
        }
        
        // Calculate cancellation charges based on policy
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckInDate());
        double totalAmount = reservation.getRoom().getPricePerNight() * reservation.getNumberOfNights();
        
        double cancellationCharge;
        double refundAmount;
        String policy;
        
        if (daysUntilCheckIn > 2) {
            // Free cancellation if more than 48 hours before check-in
            cancellationCharge = 0.0;
            refundAmount = totalAmount;
            policy = "Free cancellation - more than 48 hours before check-in";
        } else if (daysUntilCheckIn >= 1) {
            // 20% charge if 24-48 hours before check-in
            cancellationCharge = totalAmount * 0.20;
            refundAmount = totalAmount - cancellationCharge;
            policy = "20% cancellation charge for cancellations within 48 hours of check-in";
        } else {
            // 50% charge if less than 24 hours or on check-in day
            cancellationCharge = totalAmount * 0.50;
            refundAmount = totalAmount - cancellationCharge;
            policy = "50% cancellation charge for cancellations within 24 hours of check-in";
        }
        
        // Create cancellation record
        Cancellation cancellation = new Cancellation();
        cancellation.setCancellationId("CXL-" + LocalDateTime.now().getYear() + "-" + 
                                      confirmationNumber.substring(4));
        cancellation.setReservation(reservation);
        cancellation.setCancellationCharge(cancellationCharge);
        cancellation.setRefundAmount(refundAmount);
        cancellation.setReason(reason);
        cancellation.setCancellationPolicy(policy);
        cancellation.setCancellationTime(LocalDateTime.now());
        
        cancellationRepository.save(cancellation);
        
        // Update reservation status
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        
        // Update room status to available
        Room room = reservation.getRoom();
        room.setStatus(Room.RoomStatus.AVAILABLE);
        roomRepository.save(room);
        
        Map<String, Object> response = new HashMap<>();
        response.put("confirmationNumber", confirmationNumber);
        response.put("cancellationId", cancellation.getCancellationId());
        response.put("cancellationCharge", cancellationCharge);
        response.put("refundAmount", refundAmount);
        response.put("cancellationPolicy", policy);
        response.put("cancellationTime", cancellation.getCancellationTime());
        
        return response;
    }
    
    public Map<String, Object> getCancellationDetails(String cancellationId) {
        Cancellation cancellation = cancellationRepository.findByCancellationId(cancellationId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cancellation not found with ID: " + cancellationId));
        
        Map<String, Object> response = new HashMap<>();
        response.put("cancellationId", cancellation.getCancellationId());
        response.put("confirmationNumber", cancellation.getReservation().getConfirmationNumber());
        response.put("cancellationCharge", cancellation.getCancellationCharge());
        response.put("refundAmount", cancellation.getRefundAmount());
        response.put("reason", cancellation.getReason());
        response.put("cancellationTime", cancellation.getCancellationTime());
        
        return response;
    }
}