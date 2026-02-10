package com.hotel.management.controller;

import com.hotel.management.dto.CreateReservationRequest;
import com.hotel.management.model.Reservation;
import com.hotel.management.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/v1/reservations")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> searchReservations(
            @RequestParam(required = false) String confirmationNumber,
            @RequestParam(required = false) String guestName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) Reservation.ReservationStatus status) {
        
        Map<String, Object> response = reservationService.searchReservations(
            confirmationNumber, guestName, checkInDate, checkOutDate, status);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{confirmationNumber}")
    public ResponseEntity<Reservation> getReservationByConfirmationNumber(
            @PathVariable String confirmationNumber) {
        Reservation reservation = reservationService.getReservationByConfirmationNumber(confirmationNumber);
        return ResponseEntity.ok(reservation);
    }
}