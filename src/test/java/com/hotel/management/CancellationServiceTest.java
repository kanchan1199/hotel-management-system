package com.hotel.management;

import com.hotel.management.exception.BusinessException;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.CancellationRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import com.hotel.management.service.CancellationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CancellationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CancellationRepository cancellationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private CancellationService cancellationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCancelReservation_FreeCancellation() {
        // Arrange
        Room room = new Room();
        room.setId(1L);
        room.setPricePerNight(5000.0);

        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now().plusDays(5)); // More than 48 hours
        reservation.setNumberOfNights(3);
        reservation.setRoom(room);

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));
        when(cancellationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(roomRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Map<String, Object> result = cancellationService.cancelReservation("CNF-2024-001234", "Change of plans");

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.get("cancellationCharge"));
        assertEquals(15000.0, result.get("refundAmount")); // 5000 * 3 nights
        verify(cancellationRepository, times(1)).save(any());
    }

    @Test
    void testCancelReservation_WithCharges() {
        // Arrange
        Room room = new Room();
        room.setId(1L);
        room.setPricePerNight(5000.0);

        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now().plusDays(1)); // Within 48 hours
        reservation.setNumberOfNights(2);
        reservation.setRoom(room);

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));
        when(cancellationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(roomRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Map<String, Object> result = cancellationService.cancelReservation("CNF-2024-001234", "Emergency");

        // Assert
        assertNotNull(result);
        assertEquals(2000.0, result.get("cancellationCharge")); // 20% of 10000
        assertEquals(8000.0, result.get("refundAmount"));
    }

    @Test
    void testCancelReservation_AlreadyCheckedIn() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cancellationService.cancelReservation("CNF-2024-001234", "Test");
        });

        assertEquals("INVALID_STATUS", exception.getErrorCode());
    }

    @Test
    void testCancelReservation_AlreadyCancelled() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cancellationService.cancelReservation("CNF-2024-001234", "Test");
        });

        assertEquals("ALREADY_CANCELLED", exception.getErrorCode());
    }
}