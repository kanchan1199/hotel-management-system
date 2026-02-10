package com.hotel.management.service;

import com.hotel.management.dto.CreateReservationRequest;
import com.hotel.management.dto.GuestDTO;
import com.hotel.management.exception.BusinessException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Guest;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.GuestRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private GuestRepository guestRepository;
    
    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        // Validate dates
        if (request.getCheckOutDate().isBefore(request.getCheckInDate()) || 
            request.getCheckOutDate().isEqual(request.getCheckInDate())) {
            throw new BusinessException("INVALID_DATES", "Check-out date must be after check-in date");
        }
        
        // Find or create guest
        Guest guest = findOrCreateGuest(request.getGuest());
        
        // Find available room
        Room room;
        if (request.getRoomId() != null && !request.getRoomId().isEmpty()) {
            room = roomRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        } else {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByType(
                request.getRoomType(), request.getCheckInDate(), request.getCheckOutDate());
            
            if (availableRooms.isEmpty()) {
                throw new BusinessException("ROOM_NOT_AVAILABLE", 
                    "No rooms available for the selected dates and type");
            }
            room = availableRooms.get(0);
        }
        
        // Check if room is under maintenance
        if (room.getStatus() == Room.RoomStatus.MAINTENANCE) {
            throw new BusinessException("ROOM_MAINTENANCE", 
                "Room is under maintenance and cannot be booked");
        }
        
        // Check for double booking
        boolean isBooked = reservationRepository.isRoomBooked(
            room.getId(), request.getCheckInDate(), request.getCheckOutDate());
        
        if (isBooked) {
            throw new BusinessException("ROOM_NOT_AVAILABLE", 
                "Selected room is not available for the specified dates");
        }
        
        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber(generateConfirmationNumber());
        reservation.setRoom(room);
        reservation.setGuest(guest);
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setNumberOfNights((int) ChronoUnit.DAYS.between(
            request.getCheckInDate(), request.getCheckOutDate()));
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setCreatedAt(LocalDateTime.now());
        
        // Update room status
        room.setStatus(Room.RoomStatus.BOOKED);
        roomRepository.save(room);
        
        return reservationRepository.save(reservation);
    }
    
    public Reservation getReservationByConfirmationNumber(String confirmationNumber) {
        return reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reservation not found with confirmation number: " + confirmationNumber));
    }
    
    public Map<String, Object> searchReservations(String confirmationNumber, String guestName, 
                                                   LocalDate checkInDate, LocalDate checkOutDate,
                                                   Reservation.ReservationStatus status) {
        List<Reservation> reservations = new ArrayList<>();
        
        if (confirmationNumber != null && !confirmationNumber.isEmpty()) {
            reservationRepository.findByConfirmationNumber(confirmationNumber)
                .ifPresent(reservations::add);
        } else if (guestName != null && !guestName.isEmpty()) {
            reservations = reservationRepository.findByGuestName(guestName);
        } else if (checkInDate != null && checkOutDate != null) {
            reservations = reservationRepository.findByDateRange(checkInDate, checkOutDate);
        } else if (status != null) {
            reservations = reservationRepository.findByStatus(status);
        } else {
            reservations = reservationRepository.findAll();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("reservations", reservations);
        response.put("totalCount", reservations.size());
        
        return response;
    }
    
    private Guest findOrCreateGuest(GuestDTO guestDTO) {
        Optional<Guest> existingGuest = guestRepository.findByEmail(guestDTO.getEmail());
        
        if (existingGuest.isPresent()) {
            return existingGuest.get();
        }
        
        Guest guest = new Guest();
        guest.setGuestId("GST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        guest.setName(guestDTO.getName());
        guest.setContactNumber(guestDTO.getContactNumber());
        guest.setEmail(guestDTO.getEmail());
        guest.setIdProofType(guestDTO.getIdProofType());
        guest.setIdProofNumber(guestDTO.getIdProofNumber());
        guest.setAddress(guestDTO.getAddress());
        
        return guestRepository.save(guest);
    }
    
    private String generateConfirmationNumber() {
        return "CNF-" + LocalDate.now().getYear() + "-" + 
               String.format("%06d", new Random().nextInt(999999));
    }
}