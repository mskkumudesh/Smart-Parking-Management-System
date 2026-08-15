package com.spms.parkingservice.controller;

import com.spms.parkingservice.dto.*;
import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.Reservation;
import com.spms.parkingservice.entity.SpaceStatus;
import com.spms.parkingservice.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpaceController {

    private final ParkingSpaceService service;

    public ParkingSpaceController(ParkingSpaceService service) {
        this.service = service;
    }

    @PostMapping("/spaces")
    public ResponseEntity<ParkingSpace> create(@Valid @RequestBody SpaceCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/spaces")
    public ResponseEntity<Page<ParkingSpace>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) SpaceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.search(city, zone, status, pageable));
    }

    @GetMapping("/spaces/{id}")
    public ResponseEntity<ParkingSpace> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/spaces/{id}")
    public ResponseEntity<ParkingSpace> update(@PathVariable Long id, @RequestBody SpaceUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @PatchMapping("/spaces/{id}/status")
    public ResponseEntity<ParkingSpace> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req.getStatus()));
    }

    @DeleteMapping("/spaces/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owners/{ownerId}/spaces")
    public ResponseEntity<Page<ParkingSpace>> byOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.byOwner(ownerId, PageRequest.of(page, size)));
    }

    @PostMapping("/spaces/{id}/reserve")
    public ResponseEntity<Reservation> reserve(@PathVariable Long id, @Valid @RequestBody ReservationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.reserve(id, req));
    }

    @PostMapping("/reservations/{id}/release")
    public ResponseEntity<Reservation> release(@PathVariable Long id) {
        return ResponseEntity.ok(service.release(id));
    }

    @GetMapping("/spaces/{id}/reservations")
    public ResponseEntity<List<Reservation>> reservationsForSpace(@PathVariable Long id) {
        return ResponseEntity.ok(service.reservationsForSpace(id));
    }
}
