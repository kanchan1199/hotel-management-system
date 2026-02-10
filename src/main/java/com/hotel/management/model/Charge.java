package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "charges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String chargeId;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargeType chargeType;
    
    @Column(nullable = false)
    private LocalDateTime chargeDate;
    
    public enum ChargeType {
        ROOM_CHARGE, SERVICE_CHARGE, TAX, LATE_CHECKOUT, OTHER
    }
}