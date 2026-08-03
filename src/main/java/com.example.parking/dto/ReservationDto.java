package com.example.parking.dto;

import java.time.LocalDateTime;

public record ReservationDto(
        Long id,
        Long parkingSpotId,
        String spotNumber,
        String applicantName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}