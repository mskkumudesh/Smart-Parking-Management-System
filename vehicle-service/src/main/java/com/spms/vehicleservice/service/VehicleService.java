package com.spms.vehicleservice.service;

import com.spms.vehicleservice.dto.EntryRequest;
import com.spms.vehicleservice.dto.VehicleRegisterRequest;
import com.spms.vehicleservice.dto.VehicleUpdateRequest;
import com.spms.vehicleservice.entity.EntryExitLog;
import com.spms.vehicleservice.entity.LogStatus;
import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.exception.DuplicateResourceException;
import com.spms.vehicleservice.exception.InvalidVehicleStateException;
import com.spms.vehicleservice.exception.ResourceNotFoundException;
import com.spms.vehicleservice.repository.EntryExitLogRepository;
import com.spms.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final EntryExitLogRepository logRepository;

    public VehicleService(VehicleRepository vehicleRepository, EntryExitLogRepository logRepository) {
        this.vehicleRepository = vehicleRepository;
        this.logRepository = logRepository;
    }

    public Vehicle register(VehicleRegisterRequest req) {
        if (vehicleRepository.existsByPlateNumber(req.getPlateNumber())) {
            throw new DuplicateResourceException("A vehicle with plate number " + req.getPlateNumber() + " already exists");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(req.getUserId());
        vehicle.setPlateNumber(req.getPlateNumber());
        vehicle.setType(req.getType());
        vehicle.setColor(req.getColor());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getById(Long id) {
        return findVehicleOrThrow(id);
    }

    public boolean exists(Long id) {
        return vehicleRepository.existsById(id);
    }

    public Vehicle update(Long id, VehicleUpdateRequest req) {
        Vehicle vehicle = findVehicleOrThrow(id);
        if (req.getColor() != null) vehicle.setColor(req.getColor());
        if (req.getType() != null) vehicle.setType(req.getType());
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {
        Vehicle vehicle = findVehicleOrThrow(id);
        vehicleRepository.delete(vehicle);
    }

    public List<Vehicle> byUser(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public EntryExitLog recordEntry(Long vehicleId, EntryRequest req) {
        findVehicleOrThrow(vehicleId);
        logRepository.findFirstByVehicleIdAndStatus(vehicleId, LogStatus.PARKED).ifPresent(l -> {
            throw new InvalidVehicleStateException("Vehicle " + vehicleId + " already has an open entry (log id " + l.getId() + ")");
        });
        EntryExitLog log = new EntryExitLog();
        log.setVehicleId(vehicleId);
        log.setSpaceId(req.getSpaceId());
        log.setEntryTime(LocalDateTime.now());
        log.setStatus(LogStatus.PARKED);
        return logRepository.save(log);
    }

    public EntryExitLog recordExit(Long vehicleId) {
        findVehicleOrThrow(vehicleId);
        EntryExitLog log = logRepository.findFirstByVehicleIdAndStatus(vehicleId, LogStatus.PARKED)
                .orElseThrow(() -> new InvalidVehicleStateException("Vehicle " + vehicleId + " has no open entry to close"));
        log.setExitTime(LocalDateTime.now());
        log.setStatus(LogStatus.EXITED);
        return logRepository.save(log);
    }

    public List<EntryExitLog> logsForVehicle(Long vehicleId) {
        findVehicleOrThrow(vehicleId);
        return logRepository.findByVehicleIdOrderByEntryTimeDesc(vehicleId);
    }

    private Vehicle findVehicleOrThrow(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + id + " not found"));
    }
}
