package com.spms.paymentservice.repository;

import com.spms.paymentservice.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByPaymentId(Long paymentId);
}
