package com.example.parking.controller;

import com.example.parking.dto.ParkingSpotDto;
import com.example.parking.service.ParkingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class ParkingSpotController {

    private final ParkingService parkingService;

    public ParkingSpotController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping
    public List<ParkingSpotDto> getAllSpots() {
        return parkingService.getAllSpots();
    }
}