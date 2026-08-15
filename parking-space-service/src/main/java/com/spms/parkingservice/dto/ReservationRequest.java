package com.spms.parkingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservationRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    private Long vehicleId;

    @NotNull(message = "reservedFrom is required")
    private LocalDateTime reservedFrom;

    @NotNull(message = "reservedTo is required")
    private LocalDateTime reservedTo;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public LocalDateTime getReservedFrom() { return reservedFrom; }
    public void setReservedFrom(LocalDateTime reservedFrom) { this.reservedFrom = reservedFrom; }
    public LocalDateTime getReservedTo() { return reservedTo; }
    public void setReservedTo(LocalDateTime reservedTo) { this.reservedTo = reservedTo; }
}
