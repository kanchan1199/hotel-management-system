package com.hotel.management.dto;

import com.hotel.management.model.Guest;
import com.hotel.management.model.Room;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {
    @NotNull(message = "Room type is required")
    private Room.RoomType roomType;
    
    private String roomId;
    
    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;
    
    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;
    
    @NotNull(message = "Guest details are required")
    @Valid
    private GuestDTO guest;
    
    @Positive(message = "Number of guests must be positive")
    private Integer numberOfGuests;
    
    private String specialRequests;
}