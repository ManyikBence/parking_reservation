package com.example.parking;

import com.example.parking.dto.CreateReservationRequest;
import com.example.parking.dto.ReservationDto;
import com.example.parking.exception.ReservationConflictException;
import com.example.parking.model.ParkingSpot;
import com.example.parking.model.SpotType;
import com.example.parking.repository.ParkingSpotRepository;
import com.example.parking.repository.ReservationRepository;
import com.example.parking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingServiceTest {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ParkingSpot testSpot;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        spotRepository.deleteAll();
        testSpot = spotRepository.save(new ParkingSpot(null, "TEST-01", SpotType.REGULAR, "Test Spot"));
    }

    @Test
    void testCreateReservationSuccess() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        CreateReservationRequest req = new CreateReservationRequest(testSpot.getId(), "Nagy Anna", start, end);
        ReservationDto dto = parkingService.createReservation(req);

        assertNotNull(dto.id());
        assertEquals("Nagy Anna", dto.applicantName());
    }

    @Test
    void testCreateReservationOverlapThrowsConflict() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        CreateReservationRequest req1 = new CreateReservationRequest(testSpot.getId(), "Nagy Anna", start, end);
        parkingService.createReservation(req1);

        LocalDateTime overlapStart = start.plusHours(1);
        LocalDateTime overlapEnd = end.plusHours(1);
        CreateReservationRequest req2 = new CreateReservationRequest(testSpot.getId(), "Szabó Gábor", overlapStart, overlapEnd);

        assertThrows(ReservationConflictException.class, () -> parkingService.createReservation(req2));
    }

    @Test
    void testCancelReservationSuccess() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        ReservationDto dto = parkingService.createReservation(new CreateReservationRequest(testSpot.getId(), "Nagy Anna", start, end));
        parkingService.cancelReservation(dto.id());

        var spotReservations = parkingService.getReservationsForSpot(testSpot.getId());
        assertTrue(spotReservations.isEmpty());
    }
}