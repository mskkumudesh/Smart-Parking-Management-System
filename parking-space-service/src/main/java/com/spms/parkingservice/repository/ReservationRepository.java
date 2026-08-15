package com.spms.parkingservice.repository;

import com.spms.parkingservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySpaceId(Long spaceId);
}
