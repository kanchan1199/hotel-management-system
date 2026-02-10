package com.hotel.management.controller;

import com.hotel.management.model.Room;
import com.hotel.management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/v1/rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getRoomAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) Room.RoomType roomType,
            @RequestParam(required = false) Room.RoomStatus status) {
        
        Map<String, Object> response = roomService.getRoomAvailability(
            checkInDate, checkOutDate, roomType, status);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable String roomId) {
        Room room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }
}