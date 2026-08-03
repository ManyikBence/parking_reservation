package com.example.parking.dto;

import com.example.parking.model.SpotType;

public record ParkingSpotDto(Long id, String spotNumber, SpotType spotType, String description) {}