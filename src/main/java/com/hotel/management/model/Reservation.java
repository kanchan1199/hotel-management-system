package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String confirmationNumber;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;
    
    @Column(nullable = false)
    private LocalDate checkInDate;
    
    @Column(nullable = false)
    private LocalDate checkOutDate;
    
    private Integer numberOfGuests;
    
    private Integer numberOfNights;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String specialRequests;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime checkInTime;
    
    private LocalDateTime checkOutTime;
    
    public enum ReservationStatus {
        CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    }
}