package com.spms.vehicleservice.dto;

import com.spms.vehicleservice.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRegisterRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "plateNumber is required")
    private String plateNumber;

    @NotNull(message = "type is required")
    private VehicleType type;

    private String color;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
