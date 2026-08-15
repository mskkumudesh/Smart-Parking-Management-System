package com.spms.parkingservice.service;

import com.spms.parkingservice.client.VehicleServiceClient;
import com.spms.parkingservice.dto.*;
import com.spms.parkingservice.entity.*;
import com.spms.parkingservice.exception.ResourceNotFoundException;
import com.spms.parkingservice.exception.SpaceNotAvailableException;
import com.spms.parkingservice.repository.ParkingSpaceRepository;
import com.spms.parkingservice.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.util.List;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository spaceRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleServiceClient vehicleServiceClient;

    public ParkingSpaceService(ParkingSpaceRepository spaceRepository,
                                ReservationRepository reservationRepository,
                                VehicleServiceClient vehicleServiceClient) {
        this.spaceRepository = spaceRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleServiceClient = vehicleServiceClient;
    }

    public ParkingSpace create(SpaceCreateRequest req) {
        ParkingSpace space = new ParkingSpace();
        space.setOwnerId(req.getOwnerId());
        space.setZoneCode(req.getZoneCode());
        space.setCity(req.getCity());
        space.setAddress(req.getAddress());
        space.setLatitude(req.getLatitude());
        space.setLongitude(req.getLongitude());
        space.setPricePerHour(req.getPricePerHour());
        if (req.getType() != null) space.setType(req.getType());
        return spaceRepository.save(space);
    }

    public Page<ParkingSpace> search(String city, String zone, SpaceStatus status, Pageable pageable) {
        if (city != null && zone != null && status != null) {
            return spaceRepository.findByCityIgnoreCaseAndZoneCodeIgnoreCaseAndStatus(city, zone, status, pageable);
        }
        if (city != null && status != null) {
            return spaceRepository.findByCityIgnoreCaseAndStatus(city, status, pageable);
        }
        if (city != null) {
            return spaceRepository.findByCityIgnoreCase(city, pageable);
        }
        if (status != null) {
            return spaceRepository.findByStatus(status, pageable);
        }
        return spaceRepository.findAll(pageable);
    }

    public ParkingSpace getById(Long id) {
        return findSpaceOrThrow(id);
    }

    public ParkingSpace update(Long id, SpaceUpdateRequest req) {
        ParkingSpace space = findSpaceOrThrow(id);
        if (req.getAddress() != null) space.setAddress(req.getAddress());
        if (req.getLatitude() != null) space.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) space.setLongitude(req.getLongitude());
        if (req.getPricePerHour() != null) space.setPricePerHour(req.getPricePerHour());
        if (req.getType() != null) space.setType(req.getType());
        return spaceRepository.save(space);
    }

    /**
     * Manual OR simulated-IoT status update. A Postman/cron request hitting this
     * endpoint periodically is enough to emulate real-time sensor updates.
     */
    public ParkingSpace updateStatus(Long id, SpaceStatus status) {
        ParkingSpace space = findSpaceOrThrow(id);
        space.setStatus(status);
        return spaceRepository.save(space);
    }

    public void delete(Long id) {
        ParkingSpace space = findSpaceOrThrow(id);
        spaceRepository.delete(space);
    }

    public Page<ParkingSpace> byOwner(Long ownerId, Pageable pageable) {
        return spaceRepository.findByOwnerId(ownerId, pageable);
    }

    public Reservation reserve(Long spaceId, ReservationRequest req) {
        ParkingSpace space = findSpaceOrThrow(spaceId);
        if (space.getStatus() != SpaceStatus.AVAILABLE) {
            throw new SpaceNotAvailableException("Space " + spaceId + " is not available (current status: " + space.getStatus() + ")");
        }
        if (!req.getReservedTo().isAfter(req.getReservedFrom())) {
            throw new IllegalArgumentException("reservedTo must be after reservedFrom");
        }
        if (req.getVehicleId() != null) {
            verifyVehicleExists(req.getVehicleId());
        }

        Reservation reservation = new Reservation();
        reservation.setSpaceId(spaceId);
        reservation.setUserId(req.getUserId());
        reservation.setVehicleId(req.getVehicleId());
        reservation.setReservedFrom(req.getReservedFrom());
        reservation.setReservedTo(req.getReservedTo());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation = reservationRepository.save(reservation);

        space.setStatus(SpaceStatus.RESERVED);
        spaceRepository.save(space);

        return reservation;
    }

    public Reservation release(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + reservationId + " not found"));
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

        ParkingSpace space = findSpaceOrThrow(reservation.getSpaceId());
        space.setStatus(SpaceStatus.AVAILABLE);
        spaceRepository.save(space);

        return reservation;
    }

    public List<Reservation> reservationsForSpace(Long spaceId) {
        findSpaceOrThrow(spaceId);
        return reservationRepository.findBySpaceId(spaceId);
    }

    private ParkingSpace findSpaceOrThrow(Long id) {
        return spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking space with id " + id + " not found"));
    }

    private void verifyVehicleExists(Long vehicleId) {
        boolean exists;
        try {
            exists = vehicleServiceClient.vehicleExists(vehicleId);
        } catch (FeignException ex) {
            // Vehicle service unreachable or returned an HTTP error: fail soft rather than
            // blocking the whole reservation flow. In a stricter deployment this could
            // instead rethrow to hard-fail the reservation.
            return;
        }
        if (!exists) {
            throw new ResourceNotFoundException("Vehicle with id " + vehicleId + " not found");
        }
    }
}
