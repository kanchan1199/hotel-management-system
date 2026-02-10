package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String guestId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String contactNumber;
    
    @Column(nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdProofType idProofType;
    
    @Column(nullable = false)
    private String idProofNumber;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    public enum IdProofType {
        PASSPORT, DRIVERS_LICENSE, NATIONAL_ID, AADHAAR
    }
}