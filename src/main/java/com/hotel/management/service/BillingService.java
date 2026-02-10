package com.hotel.management.service;

import com.hotel.management.dto.BillResponse;
import com.hotel.management.dto.PaymentRequest;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Charge;
import com.hotel.management.model.Payment;
import com.hotel.management.model.Reservation;
import com.hotel.management.repository.ChargeRepository;
import com.hotel.management.repository.PaymentRepository;
import com.hotel.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BillingService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ChargeRepository chargeRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    private static final double TAX_RATE = 0.12; // 12% tax
    
    public BillResponse getBill(String confirmationNumber) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reservation not found with confirmation number: " + confirmationNumber));
        
        List<Charge> charges = chargeRepository.findByReservationConfirmationNumber(confirmationNumber);
        
        return buildBillResponse(reservation, charges);
    }
    
    @Transactional
    public BillResponse calculateBill(String confirmationNumber, List<Charge> additionalCharges) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reservation not found with confirmation number: " + confirmationNumber));
        
        // Add room charges if not already added
        List<Charge> existingCharges = chargeRepository.findByReservationConfirmationNumber(confirmationNumber);
        boolean hasRoomCharge = existingCharges.stream()
            .anyMatch(c -> c.getChargeType() == Charge.ChargeType.ROOM_CHARGE);
        
        if (!hasRoomCharge) {
            double roomTotal = reservation.getRoom().getPricePerNight() * reservation.getNumberOfNights();
            Charge roomCharge = new Charge();
            roomCharge.setChargeId("CHG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            roomCharge.setReservation(reservation);
            roomCharge.setDescription("Room charges for " + reservation.getNumberOfNights() + " nights");
            roomCharge.setAmount(roomTotal);
            roomCharge.setChargeType(Charge.ChargeType.ROOM_CHARGE);
            roomCharge.setChargeDate(LocalDateTime.now());
            chargeRepository.save(roomCharge);
            existingCharges.add(roomCharge);
        }
        
        // Add additional charges if provided
        if (additionalCharges != null && !additionalCharges.isEmpty()) {
            for (Charge charge : additionalCharges) {
                charge.setReservation(reservation);
                charge.setChargeId("CHG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                charge.setChargeDate(LocalDateTime.now());
                chargeRepository.save(charge);
                existingCharges.add(charge);
            }
        }
        
        return buildBillResponse(reservation, existingCharges);
    }
    
    @Transactional
    public Payment recordPayment(PaymentRequest request) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(request.getConfirmationNumber())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reservation not found with confirmation number: " + request.getConfirmationNumber()));
        
        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + LocalDateTime.now().getYear() + "-" + 
                           String.format("%06d", new Random().nextInt(999999)));
        payment.setReservation(reservation);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionReference(request.getTransactionReference());
        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setReceiptNumber("RCP-" + LocalDateTime.now().getYear() + "-" + 
                               String.format("%06d", new Random().nextInt(999999)));
        
        return paymentRepository.save(payment);
    }
    
    public Map<String, Object> getPaymentHistory(String confirmationNumber) {
        List<Payment> payments = paymentRepository.findByReservationConfirmationNumber(confirmationNumber);
        
        double totalPaid = payments.stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("totalPaid", totalPaid);
        
        return response;
    }
    
    private BillResponse buildBillResponse(Reservation reservation, List<Charge> charges) {
        double roomCharges = charges.stream()
            .filter(c -> c.getChargeType() == Charge.ChargeType.ROOM_CHARGE)
            .mapToDouble(Charge::getAmount)
            .sum();
        
        double serviceCharges = charges.stream()
            .filter(c -> c.getChargeType() == Charge.ChargeType.SERVICE_CHARGE)
            .mapToDouble(Charge::getAmount)
            .sum();
        
        double lateCheckoutCharge = charges.stream()
            .filter(c -> c.getChargeType() == Charge.ChargeType.LATE_CHECKOUT)
            .mapToDouble(Charge::getAmount)
            .sum();
        
        double subtotal = roomCharges + serviceCharges + lateCheckoutCharge;
        double taxAmount = subtotal * TAX_RATE;
        double totalAmount = subtotal + taxAmount;
        
        // Check payment status
        double totalPaid = paymentRepository.findByReservationConfirmationNumber(reservation.getConfirmationNumber())
            .stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        
        String paymentStatus;
        if (totalPaid >= totalAmount) {
            paymentStatus = "PAID";
        } else if (totalPaid > 0) {
            paymentStatus = "PARTIAL";
        } else {
            paymentStatus = "PENDING";
        }
        
        BillResponse response = new BillResponse();
        response.setBillId("BILL-" + LocalDateTime.now().getYear() + "-" + 
                          reservation.getConfirmationNumber().substring(4));
        response.setConfirmationNumber(reservation.getConfirmationNumber());
        response.setCharges(charges);
        response.setRoomCharges(roomCharges);
        response.setServiceCharges(serviceCharges);
        response.setTaxAmount(taxAmount);
        response.setLateCheckOutCharge(lateCheckoutCharge);
        response.setTotalAmount(totalAmount);
        response.setPaymentStatus(paymentStatus);
        response.setGeneratedAt(LocalDateTime.now());
        
        return response;
    }
}