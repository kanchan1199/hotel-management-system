package com.hotel.management.controller;

import com.hotel.management.service.CancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/cancellations")
public class CancellationController {
    
    @Autowired
    private CancellationService cancellationService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> cancelReservation(@RequestBody Map<String, String> request) {
        String confirmationNumber = request.get("confirmationNumber");
        String reason = request.get("reason");
        
        Map<String, Object> response = cancellationService.cancelReservation(confirmationNumber, reason);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{cancellationId}")
    public ResponseEntity<Map<String, Object>> getCancellationDetails(@PathVariable String cancellationId) {
        Map<String, Object> response = cancellationService.getCancellationDetails(cancellationId);
        return ResponseEntity.ok(response);
    }
}