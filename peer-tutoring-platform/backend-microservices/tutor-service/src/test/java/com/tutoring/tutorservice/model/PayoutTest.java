package com.tutoring.tutorservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PayoutTest {

    private Payout payout;

    @BeforeEach
    void setUp() {
        payout = new Payout();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        payout.setId(1L);
        assertEquals(1L, payout.getId());
    }

    @Test
    @DisplayName("Should set and get tutor ID")
    void setTutorIdAndGetTutorId_WorksCorrectly() {
        payout.setTutorId(1L);
        assertEquals(1L, payout.getTutorId());
    }

    @Test
    @DisplayName("Should set and get amount")
    void setAmountAndGetAmount_WorksCorrectly() {
        BigDecimal amount = BigDecimal.valueOf(100.00);
        payout.setAmount(amount);
        assertEquals(amount, payout.getAmount());
    }

    @Test
    @DisplayName("Should set and get payment method")
    void setPaymentMethodAndGetPaymentMethod_WorksCorrectly() {
        payout.setPaymentMethod("Bank Transfer");
        assertEquals("Bank Transfer", payout.getPaymentMethod());
    }

    @Test
    @DisplayName("Should set and get notes")
    void setNotesAndGetNotes_WorksCorrectly() {
        payout.setNotes("Weekly payout");
        assertEquals("Weekly payout", payout.getNotes());
    }

    @Test
    @DisplayName("Should set and get status")
    void setStatusAndGetStatus_WorksCorrectly() {
        payout.setStatus(Payout.PayoutStatus.COMPLETED);
        assertEquals(Payout.PayoutStatus.COMPLETED, payout.getStatus());
    }

    @Test
    @DisplayName("Should have default PENDING status")
    void defaultStatus_IsPending() {
        Payout newPayout = new Payout();
        assertEquals(Payout.PayoutStatus.PENDING, newPayout.getStatus());
    }

    @Test
    @DisplayName("Should set and get processed by")
    void setProcessedByAndGetProcessedBy_WorksCorrectly() {
        payout.setProcessedBy(2L);
        assertEquals(2L, payout.getProcessedBy());
    }

    @Test
    @DisplayName("Should set and get processed at")
    void setProcessedAtAndGetProcessedAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        payout.setProcessedAt(now);
        assertEquals(now, payout.getProcessedAt());
    }

    @Test
    @DisplayName("Should get created at timestamp - initially null before persistence")
    void getCreatedAt_InitiallyNull() {
        // @CreationTimestamp only works with JPA persistence
        assertNull(payout.getCreatedAt());
    }

    @Test
    @DisplayName("Should test all payout status enum values")
    void payoutStatusEnum_HasAllValues() {
        assertEquals(3, Payout.PayoutStatus.values().length);
        assertNotNull(Payout.PayoutStatus.PENDING);
        assertNotNull(Payout.PayoutStatus.COMPLETED);
        assertNotNull(Payout.PayoutStatus.FAILED);
    }
}
