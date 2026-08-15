package com.spms.parkingservice.repository;

import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.SpaceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    Page<ParkingSpace> findByCityIgnoreCaseAndZoneCodeIgnoreCaseAndStatus(
            String city, String zoneCode, SpaceStatus status, Pageable pageable);
    Page<ParkingSpace> findByCityIgnoreCaseAndStatus(String city, SpaceStatus status, Pageable pageable);
    Page<ParkingSpace> findByCityIgnoreCase(String city, Pageable pageable);
    Page<ParkingSpace> findByStatus(SpaceStatus status, Pageable pageable);
    Page<ParkingSpace> findByOwnerId(Long ownerId, Pageable pageable);
}
