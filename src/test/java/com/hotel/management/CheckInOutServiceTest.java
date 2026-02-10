package com.hotel.management;

import com.hotel.management.exception.BusinessException;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.ChargeRepository;
import com.hotel.management.repository.PaymentRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import com.hotel.management.service.BillingService;
import com.hotel.management.service.CheckInOutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CheckInOutServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BillingService billingService;

    @InjectMocks
    private CheckInOutService checkInOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckIn_Success() {
        // Arrange
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setStatus(Room.RoomStatus.BOOKED);

        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now());
        reservation.setRoom(room);

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(roomRepository.save(any(Room.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Map<String, Object> result = checkInOutService.checkIn("CNF-2024-001234", true, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("reservation"));
        assertTrue(result.containsKey("roomKey"));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCheckIn_EarlyCheckIn() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now().plusDays(2)); // Future date

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            checkInOutService.checkIn("CNF-2024-001234", true, null);
        });

        assertEquals("EARLY_CHECK_IN", exception.getErrorCode());
    }

    @Test
    void testCheckIn_InvalidStatus() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setConfirmationNumber("CNF-2024-001234");
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        reservation.setCheckInDate(LocalDate.now());

        when(reservationRepository.findByConfirmationNumber(anyString())).thenReturn(Optional.of(reservation));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            checkInOutService.checkIn("CNF-2024-001234", true, null);
        });

        assertEquals("INVALID_STATUS", exception.getErrorCode());
    }
}