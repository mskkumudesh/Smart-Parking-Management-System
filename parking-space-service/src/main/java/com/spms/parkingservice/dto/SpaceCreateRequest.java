package com.spms.parkingservice.dto;

import com.spms.parkingservice.entity.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class SpaceCreateRequest {
    @NotNull(message = "ownerId is required")
    private Long ownerId;

    @NotBlank(message = "zoneCode is required")
    private String zoneCode;

    @NotBlank(message = "city is required")
    private String city;

    private String address;
    private Double latitude;
    private Double longitude;

    @NotNull(message = "pricePerHour is required")
    @Positive(message = "pricePerHour must be positive")
    private BigDecimal pricePerHour;

    private SpaceType type;

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getZoneCode() { return zoneCode; }
    public void setZoneCode(String zoneCode) { this.zoneCode = zoneCode; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public BigDecimal getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }
    public SpaceType getType() { return type; }
    public void setType(SpaceType type) { this.type = type; }
}
