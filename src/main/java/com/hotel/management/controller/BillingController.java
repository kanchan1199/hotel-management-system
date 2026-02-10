package com.hotel.management.controller;

import com.hotel.management.dto.BillResponse;
import com.hotel.management.dto.PaymentRequest;
import com.hotel.management.model.Charge;
import com.hotel.management.model.Payment;
import com.hotel.management.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class BillingController {
    
    @Autowired
    private BillingService billingService;
    
    @GetMapping("/billing/{confirmationNumber}")
    public ResponseEntity<BillResponse> getBill(@PathVariable String confirmationNumber) {
        BillResponse bill = billingService.getBill(confirmationNumber);
        return ResponseEntity.ok(bill);
    }
    
    @PostMapping("/billing/{confirmationNumber}/calculate")
    public ResponseEntity<BillResponse> calculateBill(
            @PathVariable String confirmationNumber,
            @RequestBody(required = false) Map<String, List<Charge>> request) {
        
        List<Charge> additionalCharges = null;
        if (request != null && request.containsKey("additionalCharges")) {
            additionalCharges = request.get("additionalCharges");
        }
        
        BillResponse bill = billingService.calculateBill(confirmationNumber, additionalCharges);
        return ResponseEntity.ok(bill);
    }
    
    @PostMapping("/payments")
    public ResponseEntity<Payment> recordPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = billingService.recordPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    
    @GetMapping("/payments/{confirmationNumber}")
    public ResponseEntity<Map<String, Object>> getPaymentHistory(@PathVariable String confirmationNumber) {
        Map<String, Object> response = billingService.getPaymentHistory(confirmationNumber);
        return ResponseEntity.ok(response);
    }
}