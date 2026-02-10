package com.hotel.management;

import com.hotel.management.dto.CreateReservationRequest;
import com.hotel.management.dto.GuestDTO;
import com.hotel.management.exception.BusinessException;
import com.hotel.management.model.Guest;
import com.hotel.management.model.Reservation;
import com.hotel.management.model.Room;
import com.hotel.management.repository.GuestRepository;
import com.hotel.management.repository.ReservationRepository;
import com.hotel.management.repository.RoomRepository;
import com.hotel.management.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation_Success() {
        // Arrange
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRoomType(Room.RoomType.DELUXE);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(4));
        request.setNumberOfGuests(2);
        
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("John Doe");
        guestDTO.setEmail("john.doe@email.com");
        guestDTO.setContactNumber("+1234567890");
        guestDTO.setIdProofType(Guest.IdProofType.PASSPORT);
        guestDTO.setIdProofNumber("AB123456");
        request.setGuest(guestDTO);

        Room room = new Room();
        room.setId(1L);
        room.setRoomId("RM-101");
        room.setRoomType(Room.RoomType.DELUXE);
        room.setStatus(Room.RoomStatus.AVAILABLE);
        room.setPricePerNight(5000.0);

        Guest guest = new Guest();
        guest.setId(1L);
        guest.setGuestId("GST-001");
        guest.setEmail("john.doe@email.com");

        when(guestRepository.findByEmail(anyString())).thenReturn(Optional.of(guest));
        when(roomRepository.findAvailableRoomsByType(any(), any(), any())).thenReturn(Arrays.asList(room));
        when(reservationRepository.isRoomBooked(anyLong(), any(), any())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Reservation result = reservationService.createReservation(request);

        // Assert
        assertNotNull(result);
        assertEquals(Reservation.ReservationStatus.CONFIRMED, result.getStatus());
        assertEquals(3, result.getNumberOfNights());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_InvalidDates() {
        // Arrange
        CreateReservationRequest request = new CreateReservationRequest();
        request.setCheckInDate(LocalDate.now().plusDays(5));
        request.setCheckOutDate(LocalDate.now().plusDays(2)); // Before check-in

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            reservationService.createReservation(request);
        });

        assertEquals("INVALID_DATES", exception.getErrorCode());
    }

    @Test
    void testCreateReservation_RoomNotAvailable() {
        // Arrange
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRoomType(Room.RoomType.DELUXE);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(4));
        
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setEmail("test@email.com");
        request.setGuest(guestDTO);

        Guest guest = new Guest();
        guest.setEmail("test@email.com");

        when(guestRepository.findByEmail(anyString())).thenReturn(Optional.of(guest));
        when(roomRepository.findAvailableRoomsByType(any(), any(), any())).thenReturn(Arrays.asList());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            reservationService.createReservation(request);
        });

        assertEquals("ROOM_NOT_AVAILABLE", exception.getErrorCode());
    }

    @Test
    void testCreateReservation_RoomUnderMaintenance() {
        // Arrange
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRoomId("RM-101");
        request.setRoomType(Room.RoomType.DELUXE);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(4));
        
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setEmail("test@email.com");
        request.setGuest(guestDTO);

        Room room = new Room();
        room.setId(1L);
        room.setRoomId("RM-101");
        room.setStatus(Room.RoomStatus.MAINTENANCE);

        Guest guest = new Guest();
        guest.setEmail("test@email.com");

        when(guestRepository.findByEmail(anyString())).thenReturn(Optional.of(guest));
        when(roomRepository.findByRoomId(anyString())).thenReturn(Optional.of(room));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            reservationService.createReservation(request);
        });

        assertEquals("ROOM_MAINTENANCE", exception.getErrorCode());
    }
}