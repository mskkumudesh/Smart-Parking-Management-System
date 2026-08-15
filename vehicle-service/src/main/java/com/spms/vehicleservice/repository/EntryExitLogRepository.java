package com.spms.vehicleservice.repository;

import com.spms.vehicleservice.entity.EntryExitLog;
import com.spms.vehicleservice.entity.LogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryExitLogRepository extends JpaRepository<EntryExitLog, Long> {
    List<EntryExitLog> findByVehicleIdOrderByEntryTimeDesc(Long vehicleId);
    Optional<EntryExitLog> findFirstByVehicleIdAndStatus(Long vehicleId, LogStatus status);
}
