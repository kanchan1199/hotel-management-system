package com.hotel.management.service;

import com.hotel.management.dto.BillResponse;
import com.hotel.management.exception.BusinessException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Charge;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.ChargeRepository;
import com.hotel.management.repository.PaymentRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CheckInOutService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ChargeRepository chargeRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BillingService billingService;
    
    private static final LocalTime STANDARD_CHECKOUT_TIME = LocalTime.of(12, 0);
    private static final double LATE_CHECKOUT_CHARGE_PER_HOUR = 50.0;
    
    @Transactional
    public Map<String, Object> checkIn(String confirmationNumber, Boolean idProofVerified, String specialRequests) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No reservation found with ID " + confirmationNumber));
        
        // Business Rule: Cannot check in before reservation start date
        if (LocalDate.now().isBefore(reservation.getCheckInDate())) {
            throw new BusinessException("EARLY_CHECK_IN", 
                "Cannot check in before reservation start date");
        }
        
        if (reservation.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            throw new BusinessException("INVALID_STATUS", 
                "Reservation is not in confirmed status");
        }
        
        // Update reservation
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        reservation.setCheckInTime(LocalDateTime.now());
        if (specialRequests != null) {
            reservation.setSpecialRequests(specialRequests);
        }
        
        // Update room status
        Room room = reservation.getRoom();
        room.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(room);
        
        reservationRepository.save(reservation);
        
        Map<String, Object> response = new HashMap<>();
        response.put("reservation", reservation);
        response.put("roomKey", "KEY-" + room.getRoomNumber() + "-" + 
                    LocalDate.now().getYear() + "-" + String.format("%03d", new Random().nextInt(999)));
        response.put("checkInTime", reservation.getCheckInTime());
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> checkOut(String confirmationNumber, LocalDateTime checkOutTime) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No reservation found with ID " + confirmationNumber));
        
        if (reservation.getStatus() != Reservation.ReservationStatus.CHECKED_IN) {
            throw new BusinessException("INVALID_STATUS", 
                "Guest is not checked in");
        }
        
        // Calculate late checkout charge
        LocalDateTime actualCheckOutTime = checkOutTime != null ? checkOutTime : LocalDateTime.now();
        double lateCheckOutCharge = calculateLateCheckoutCharge(actualCheckOutTime);
        
        if (lateCheckOutCharge > 0) {
            Charge charge = new Charge();
            charge.setChargeId("CHG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            charge.setReservation(reservation);
            charge.setDescription("Late checkout charge");
            charge.setAmount(lateCheckOutCharge);
            charge.setChargeType(Charge.ChargeType.LATE_CHECKOUT);
            charge.setChargeDate(actualCheckOutTime);
            chargeRepository.save(charge);
        }
        
        // Generate final bill
        BillResponse finalBill = billingService.calculateBill(confirmationNumber, null);
        
        // Check if payment is complete
        double totalPaid = paymentRepository.findByReservationConfirmationNumber(confirmationNumber)
            .stream()
            .mapToDouble(p -> p.getAmount())
            .sum();
        
        if (totalPaid < finalBill.getTotalAmount()) {
            throw new BusinessException("UNPAID_BILL", 
                "Payment must be settled before check-out completion");
        }
        
        // Update reservation
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        reservation.setCheckOutTime(actualCheckOutTime);
        
        // Update room status
        Room room = reservation.getRoom();
        room.setStatus(Room.RoomStatus.AVAILABLE);
        roomRepository.save(room);
        
        reservationRepository.save(reservation);
        
        Map<String, Object> response = new HashMap<>();
        response.put("reservation", reservation);
        response.put("finalBill", finalBill);
        response.put("checkOutTime", actualCheckOutTime);
        response.put("lateCheckOutCharge", lateCheckOutCharge);
        
        return response;
    }
    
    private double calculateLateCheckoutCharge(LocalDateTime checkOutTime) {
        LocalTime checkOutTimeOnly = checkOutTime.toLocalTime();
        
        if (checkOutTimeOnly.isAfter(STANDARD_CHECKOUT_TIME)) {
            long hoursLate = ChronoUnit.HOURS.between(STANDARD_CHECKOUT_TIME, checkOutTimeOnly);
            if (checkOutTimeOnly.getMinute() > 0) {
                hoursLate++; // Round up partial hours
            }
            return hoursLate * LATE_CHECKOUT_CHARGE_PER_HOUR;
        }
        
        return 0.0;
    }
}