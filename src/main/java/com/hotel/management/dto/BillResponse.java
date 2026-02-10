package com.hotel.management.dto;

import com.hotel.management.model.Charge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private String billId;
    private String confirmationNumber;
    private List<Charge> charges;
    private Double roomCharges;
    private Double serviceCharges;
    private Double taxAmount;
    private Double lateCheckOutCharge;
    private Double totalAmount;
    private String paymentStatus;
    private LocalDateTime generatedAt;
}