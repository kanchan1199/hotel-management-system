package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String roomId;
    
    @Column(unique = true, nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;
    
    @Column(nullable = false)
    private Double pricePerNight;
    
    private Integer floor;
    
    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities;
    
    private Integer maxOccupancy;
    
    public enum RoomType {
        SINGLE, DOUBLE, SUITE, DELUXE
    }
    
    public enum RoomStatus {
        AVAILABLE, BOOKED, OCCUPIED, MAINTENANCE
    }
}