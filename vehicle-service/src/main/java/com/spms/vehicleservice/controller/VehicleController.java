package com.spms.vehicleservice.controller;

import com.spms.vehicleservice.dto.EntryRequest;
import com.spms.vehicleservice.dto.VehicleRegisterRequest;
import com.spms.vehicleservice.dto.VehicleUpdateRequest;
import com.spms.vehicleservice.entity.EntryExitLog;
import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Vehicle> register(@Valid @RequestBody VehicleRegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Lightweight existence check consumed by Parking Space Service (via Feign)
     * before confirming a reservation against a vehicle.
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(service.exists(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody VehicleUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.byUser(userId));
    }

    @PostMapping("/{id}/entry")
    public ResponseEntity<EntryExitLog> recordEntry(@PathVariable Long id, @RequestBody(required = false) EntryRequest req) {
        EntryRequest body = (req != null) ? req : new EntryRequest();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.recordEntry(id, body));
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<EntryExitLog> recordExit(@PathVariable Long id) {
        return ResponseEntity.ok(service.recordExit(id));
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<EntryExitLog>> logs(@PathVariable Long id) {
        return ResponseEntity.ok(service.logsForVehicle(id));
    }
}
