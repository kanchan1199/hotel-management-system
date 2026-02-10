package com.hotel.management.service;

import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Room;
import com.hotel.management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Map<String, Object> getRoomAvailability(LocalDate checkInDate, LocalDate checkOutDate, 
                                                    Room.RoomType roomType, Room.RoomStatus status) {
        List<Room> rooms;
        
        if (roomType != null) {
            rooms = roomRepository.findAvailableRoomsByType(roomType, checkInDate, checkOutDate);
        } else {
            rooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", rooms);
        response.put("totalAvailable", rooms.size());
        
        return response;
    }
    
    public Room getRoomById(String roomId) {
        return roomRepository.findByRoomId(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
    }
    
    public Room getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with number: " + roomNumber));
    }
}