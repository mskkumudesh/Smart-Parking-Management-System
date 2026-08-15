package com.spms.parkingservice.dto;

import com.spms.parkingservice.entity.SpaceStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull(message = "status is required")
    private SpaceStatus status;

    public SpaceStatus getStatus() { return status; }
    public void setStatus(SpaceStatus status) { this.status = status; }
}
