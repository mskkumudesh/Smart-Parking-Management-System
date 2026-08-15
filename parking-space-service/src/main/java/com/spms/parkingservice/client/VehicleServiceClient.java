package com.spms.parkingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Declarative REST client resolved via Eureka (name matches spring.application.name
 * of the target service, no hardcoded host/port). Used to confirm a vehicle exists
 * before confirming a reservation against it.
 */
@FeignClient(name = "vehicle-service")
public interface VehicleServiceClient {

    @GetMapping("/api/vehicles/{id}/exists")
    boolean vehicleExists(@PathVariable("id") Long id);
}
