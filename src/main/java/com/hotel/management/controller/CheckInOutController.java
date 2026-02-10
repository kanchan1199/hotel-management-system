package com.hotel.management.controller;

import com.hotel.management.service.CheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class CheckInOutController {
    
    @Autowired
    private CheckInOutService checkInOutService;
    
    @PostMapping("/check-in")
    public ResponseEntity<Map<String, Object>> checkIn(@RequestBody Map<String, Object> request) {
        String confirmationNumber = (String) request.get("confirmationNumber");
        Boolean idProofVerified = (Boolean) request.getOrDefault("idProofVerified", false);
        String specialRequests = (String) request.get("specialRequests");
        
        Map<String, Object> response = checkInOutService.checkIn(
            confirmationNumber, idProofVerified, specialRequests);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/check-out")
    public ResponseEntity<Map<String, Object>> checkOut(@RequestBody Map<String, Object> request) {
        String confirmationNumber = (String) request.get("confirmationNumber");
        String checkOutTimeStr = (String) request.get("checkOutTime");
        
        LocalDateTime checkOutTime = null;
        if (checkOutTimeStr != null) {
            checkOutTime = LocalDateTime.parse(checkOutTimeStr);
        }
        
        Map<String, Object> response = checkInOutService.checkOut(confirmationNumber, checkOutTime);
        return ResponseEntity.ok(response);
    }
}