package com.spms.paymentservice.controller;

import com.spms.paymentservice.dto.PaymentRequest;
import com.spms.paymentservice.entity.Payment;
import com.spms.paymentservice.entity.Receipt;
import com.spms.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Payment> initiate(@Valid @RequestBody PaymentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.initiate(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Payment>> byUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.byUser(userId, pageable));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refund(@PathVariable Long id) {
        return ResponseEntity.ok(service.refund(id));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Receipt> receipt(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReceipt(id));
    }
}
