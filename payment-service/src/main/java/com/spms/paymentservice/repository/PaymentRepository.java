package com.spms.paymentservice.repository;

import com.spms.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
