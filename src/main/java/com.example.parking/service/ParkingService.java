package com.example.parking.service;

import com.example.parking.dto.CreateReservationRequest;
import com.example.parking.dto.ParkingSpotDto;
import com.example.parking.dto.ReservationDto;
import com.example.parking.exception.InvalidReservationException;
import com.example.parking.exception.ReservationConflictException;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.model.ParkingSpot;
import com.example.parking.model.Reservation;
import com.example.parking.repository.ParkingSpotRepository;
import com.example.parking.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingService {

    private final ParkingSpotRepository spotRepository;
    private final ReservationRepository reservationRepository;

    public ParkingService(ParkingSpotRepository spotRepository, ReservationRepository reservationRepository) {
        this.spotRepository = spotRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ParkingSpotDto> getAllSpots() {
        return spotRepository.findAll().stream()
                .map(s -> new ParkingSpotDto(s.getId(), s.getSpotNumber(), s.getSpotType(), s.getDescription()))
                .toList();
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new InvalidReservationException("End time must be after start time");
        }

        ParkingSpot spot = spotRepository.findById(request.parkingSpotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking spot not found with id: " + request.parkingSpotId()));

        boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                spot.getId(), request.startTime(), request.endTime());

        if (hasOverlap) {
            throw new ReservationConflictException("Parking spot is already reserved for the requested time slot");
        }

        Reservation reservation = new Reservation(spot, request.applicantName(), request.startTime(), request.endTime());
        Reservation saved = reservationRepository.save(reservation);

        return mapToDto(saved);
    }

    public List<ReservationDto> getReservationsForSpot(Long spotId) {
        if (!spotRepository.existsById(spotId)) {
            throw new ResourceNotFoundException("Parking spot not found with id: " + spotId);
        }
        return reservationRepository.findByParkingSpotIdAndCancelledFalseOrderByStartTimeAsc(spotId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        if (reservation.isCancelled()) {
            throw new InvalidReservationException("Reservation is already cancelled");
        }

        reservation.setCancelled(true);
        reservationRepository.save(reservation);
    }

    private ReservationDto mapToDto(Reservation r) {
        return new ReservationDto(
                r.getId(),
                r.getParkingSpot().getId(),
                r.getParkingSpot().getSpotNumber(),
                r.getApplicantName(),
                r.getStartTime(),
                r.getEndTime(),
                r.isCancelled()
        );
    }
}