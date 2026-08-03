package com.example.parking.controller;

import com.example.parking.dto.CreateReservationRequest;
import com.example.parking.dto.ReservationDto;
import com.example.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ParkingService parkingService;

    public ReservationController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@Valid @RequestBody CreateReservationRequest request) {
        return parkingService.createReservation(request);
    }

    @GetMapping("/spot/{spotId}")
    public List<ReservationDto> getReservationsForSpot(@PathVariable Long spotId) {
        return parkingService.getReservationsForSpot(spotId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        parkingService.cancelReservation(id);
    }
}