package com.spms.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Single entry point for all SPMS client traffic.
 * Routes /api/users/**, /api/parking/**, /api/vehicles/**, /api/payments/**
 * to the corresponding microservice, resolved dynamically via Eureka.
 * Route definitions live in the Config Server (api-gateway.yml).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
