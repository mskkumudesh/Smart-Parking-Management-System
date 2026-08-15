package com.spms.paymentservice.service;

import com.spms.paymentservice.dto.PaymentRequest;
import com.spms.paymentservice.entity.*;
import com.spms.paymentservice.exception.InvalidRefundException;
import com.spms.paymentservice.exception.PaymentFailedException;
import com.spms.paymentservice.exception.ResourceNotFoundException;
import com.spms.paymentservice.repository.PaymentRepository;
import com.spms.paymentservice.repository.ReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;

    public PaymentService(PaymentRepository paymentRepository, ReceiptRepository receiptRepository) {
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
    }

    public Payment initiate(PaymentRequest req) {
        Payment payment = new Payment();
        payment.setUserId(req.getUserId());
        payment.setReservationId(req.getReservationId());
        payment.setAmount(req.getAmount());
        payment.setMethod(req.getMethod());
        payment.setTransactionRef(generateTransactionRef());

        boolean approved = simulateGateway(req);
        payment.setStatus(approved ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment = paymentRepository.save(payment);

        if (!approved) {
            throw new PaymentFailedException("Payment declined by mock gateway for transaction " + payment.getTransactionRef());
        }

        generateReceipt(payment, req);
        return payment;
    }

    public Payment getById(Long id) {
        return findPaymentOrThrow(id);
    }

    public Page<Payment> byUser(Long userId, Pageable pageable) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Payment refund(Long id) {
        Payment payment = findPaymentOrThrow(id);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidRefundException("Only SUCCESS payments can be refunded (current status: " + payment.getStatus() + ")");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }

    public Receipt getReceipt(Long paymentId) {
        findPaymentOrThrow(paymentId);
        return receiptRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("No receipt found for payment " + paymentId));
    }

    private Payment findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with id " + id + " not found"));
    }

    /**
     * Mock payment gateway. CARD payments are validated with a Luhn check on the
     * card number and a basic expiry format check; other methods always succeed.
     * This simulates a real processor without calling out to one.
     */
    private boolean simulateGateway(PaymentRequest req) {
        if (req.getMethod() == PaymentMethod.CARD) {
            return isValidCardNumber(req.getCardNumber()) && isValidExpiry(req.getCardExpiry());
        }
        return true;
    }

    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null) return false;
        String digitsOnly = cardNumber.replaceAll("\\s+", "");
        if (!digitsOnly.matches("\\d{12,19}")) return false;
        return passesLuhnCheck(digitsOnly);
    }

    private boolean passesLuhnCheck(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    private boolean isValidExpiry(String expiry) {
        return expiry != null && expiry.matches("(0[1-9]|1[0-2])/\\d{2}");
    }

    private String generateTransactionRef() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private void generateReceipt(Payment payment, PaymentRequest req) {
        String details = String.format(
                "{\"reservationId\":%d,\"amount\":%s,\"method\":\"%s\",\"transactionRef\":\"%s\"}",
                payment.getReservationId(), payment.getAmount(), payment.getMethod(), payment.getTransactionRef());
        Receipt receipt = new Receipt();
        receipt.setPaymentId(payment.getId());
        receipt.setDetails(details);
        receiptRepository.save(receipt);
    }
}
