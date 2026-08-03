package com.example.parking.repository;

import com.example.parking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByParkingSpotIdAndCancelledFalseOrderByStartTimeAsc(Long parkingSpotId);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        WHERE r.parkingSpot.id = :spotId
          AND r.cancelled = false
          AND r.startTime < :endTime
          AND r.endTime > :startTime
    """)
    boolean existsOverlappingReservation(
            @Param("spotId") Long spotId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}