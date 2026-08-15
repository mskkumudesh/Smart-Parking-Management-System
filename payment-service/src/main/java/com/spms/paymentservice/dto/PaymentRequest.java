package com.spms.paymentservice.dto;

import com.spms.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "reservationId is required")
    private Long reservationId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be positive")
    private java.math.BigDecimal amount;

    @NotNull(message = "method is required")
    private PaymentMethod method;

    // Mock card fields - only relevant when method == CARD
    private String cardNumber;
    private String cardExpiry; // MM/YY
    private String cardCvv;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardExpiry() { return cardExpiry; }
    public void setCardExpiry(String cardExpiry) { this.cardExpiry = cardExpiry; }
    public String getCardCvv() { return cardCvv; }
    public void setCardCvv(String cardCvv) { this.cardCvv = cardCvv; }
}
