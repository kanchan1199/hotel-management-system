package com.hotel.management.repository;

import com.hotel.management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomId(String roomId);
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByStatus(Room.RoomStatus status);
    List<Room> findByRoomType(Room.RoomType roomType);
    List<Room> findByRoomTypeAndStatus(Room.RoomType roomType, Room.RoomStatus status);
    
    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND r.id NOT IN " +
           "(SELECT res.room.id FROM Reservation res WHERE " +
           "res.status IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "((res.checkInDate <= :checkOutDate AND res.checkOutDate > :checkInDate)))")
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDate checkInDate, 
                                   @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.status = 'AVAILABLE' AND r.id NOT IN " +
           "(SELECT res.room.id FROM Reservation res WHERE " +
           "res.status IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "((res.checkInDate <= :checkOutDate AND res.checkOutDate > :checkInDate)))")
    List<Room> findAvailableRoomsByType(@Param("roomType") Room.RoomType roomType,
                                        @Param("checkInDate") LocalDate checkInDate, 
                                        @Param("checkOutDate") LocalDate checkOutDate);
}