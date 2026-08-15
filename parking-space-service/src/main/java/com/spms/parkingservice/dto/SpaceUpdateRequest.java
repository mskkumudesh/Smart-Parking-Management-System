package com.spms.parkingservice.dto;

import com.spms.parkingservice.entity.SpaceType;
import java.math.BigDecimal;

public class SpaceUpdateRequest {
    private String address;
    private Double latitude;
    private Double longitude;
    private BigDecimal pricePerHour;
    private SpaceType type;

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
