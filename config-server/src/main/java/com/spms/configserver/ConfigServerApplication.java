package com.spms.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Centralized Configuration Server for SPMS.
 * Serves per-service YAML config from the bundled config-repo (native profile),
 * so ports, Eureka URLs and business properties can change without redeploying services.
 *
 * Try it: http://localhost:8888/user-service/default
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
