package com.spms.paymentservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Lob
    @Column(nullable = false)
    private String details;

    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public LocalDateTime getIssuedAt() { return issuedAt; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
