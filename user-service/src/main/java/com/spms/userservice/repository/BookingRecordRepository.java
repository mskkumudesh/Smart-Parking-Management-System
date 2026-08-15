package com.spms.userservice.repository;

import com.spms.userservice.entity.BookingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {
    Page<BookingRecord> findByUserIdOrderByStartTimeDesc(Long userId, Pageable pageable);
}
