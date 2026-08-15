package com.spms.userservice.dto;

import com.spms.userservice.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingRecordRequest {

    @NotNull(message = "spaceId is required")
    private Long spaceId;

    private Long vehicleId;

    @NotNull(message = "startTime is required")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotNull(message = "status is required")
    private BookingStatus status;

    public Long getSpaceId() { return spaceId; }
    public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
