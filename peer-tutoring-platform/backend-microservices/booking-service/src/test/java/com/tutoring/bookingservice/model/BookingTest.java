package com.tutoring.bookingservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        booking.setId(1L);
        assertEquals(1L, booking.getId());
    }

    @Test
    @DisplayName("Should set and get student ID")
    void setStudentIdAndGetStudentId_WorksCorrectly() {
        booking.setStudentId(1L);
        assertEquals(1L, booking.getStudentId());
    }

    @Test
    @DisplayName("Should set and get session ID")
    void setSessionIdAndGetSessionId_WorksCorrectly() {
        booking.setSessionId(1L);
        assertEquals(1L, booking.getSessionId());
    }

    @Test
    @DisplayName("Should set and get amount")
    void setAmountAndGetAmount_WorksCorrectly() {
        BigDecimal amount = BigDecimal.valueOf(50.00);
        booking.setAmount(amount);
        assertEquals(amount, booking.getAmount());
    }

    @Test
    @DisplayName("Should set and get notes")
    void setNotesAndGetNotes_WorksCorrectly() {
        booking.setNotes("Bring calculator");
        assertEquals("Bring calculator", booking.getNotes());
    }

    @Test
    @DisplayName("Should set and get status")
    void setStatusAndGetStatus_WorksCorrectly() {
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    @DisplayName("Should have default PENDING status")
    void defaultStatus_IsPending() {
        Booking newBooking = new Booking();
        assertEquals(Booking.BookingStatus.PENDING, newBooking.getStatus());
    }

    @Test
    @DisplayName("Should set and get payment status")
    void setPaymentStatusAndGetPaymentStatus_WorksCorrectly() {
        booking.setPaymentStatus(Booking.PaymentStatus.HELD);
        assertEquals(Booking.PaymentStatus.HELD, booking.getPaymentStatus());
    }

    @Test
    @DisplayName("Should have default PENDING payment status")
    void defaultPaymentStatus_IsPending() {
        Booking newBooking = new Booking();
        assertEquals(Booking.PaymentStatus.PENDING, newBooking.getPaymentStatus());
    }

    @Test
    @DisplayName("Should set and get confirmed at")
    void setConfirmedAtAndGetConfirmedAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        booking.setConfirmedAt(now);
        assertEquals(now, booking.getConfirmedAt());
    }

    @Test
    @DisplayName("Should set and get cancelled at")
    void setCancelledAtAndGetCancelledAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        booking.setCancelledAt(now);
        assertEquals(now, booking.getCancelledAt());
    }

    @Test
    @DisplayName("Should set and get completed at")
    void setCompletedAtAndGetCompletedAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        booking.setCompletedAt(now);
        assertEquals(now, booking.getCompletedAt());
    }

    @Test
    @DisplayName("Should get created at timestamp - initially null before persistence")
    void getCreatedAt_InitiallyNull() {
        assertNull(booking.getCreatedAt());
    }

    @Test
    @DisplayName("Should get updated at timestamp - initially null before persistence")
    void getUpdatedAt_InitiallyNull() {
        assertNull(booking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test all booking status enum values")
    void bookingStatusEnum_HasAllValues() {
        assertEquals(5, Booking.BookingStatus.values().length);
        assertNotNull(Booking.BookingStatus.PENDING);
        assertNotNull(Booking.BookingStatus.CONFIRMED);
        assertNotNull(Booking.BookingStatus.CANCELLED);
        assertNotNull(Booking.BookingStatus.COMPLETED);
        assertNotNull(Booking.BookingStatus.NO_SHOW);
    }

    @Test
    @DisplayName("Should test all payment status enum values")
    void paymentStatusEnum_HasAllValues() {
        assertEquals(4, Booking.PaymentStatus.values().length);
        assertNotNull(Booking.PaymentStatus.PENDING);
        assertNotNull(Booking.PaymentStatus.HELD);
        assertNotNull(Booking.PaymentStatus.RELEASED_TO_TUTOR);
        assertNotNull(Booking.PaymentStatus.REFUNDED);
    }
}
