package com.example.parking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotType spotType;

    private String description;

    public ParkingSpot() {}

    public ParkingSpot(Long id, String spotNumber, SpotType spotType, String description) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    public SpotType getSpotType() { return spotType; }
    public void setSpotType(SpotType spotType) { this.spotType = spotType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}