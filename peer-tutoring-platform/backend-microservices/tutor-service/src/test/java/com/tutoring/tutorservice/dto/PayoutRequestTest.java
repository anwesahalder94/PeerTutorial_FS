package com.tutoring.tutorservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PayoutRequestTest {

    private Validator validator;
    private PayoutRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        request = new PayoutRequest();
        request.setTutorId(1L);
        request.setAmount(BigDecimal.valueOf(100.00));
        request.setPaymentMethod("Bank Transfer");
        request.setNotes("Weekly payout");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validRequest_NoViolations() {
        Set<ConstraintViolation<PayoutRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when tutor ID is null")
    void nullTutorId_HasViolation() {
        request.setTutorId(null);
        Set<ConstraintViolation<PayoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when amount is null")
    void nullAmount_HasViolation() {
        request.setAmount(null);
        Set<ConstraintViolation<PayoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when payment method is blank")
    void blankPaymentMethod_HasViolation() {
        request.setPaymentMethod("");
        Set<ConstraintViolation<PayoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get tutor ID")
    void setAndGetTutorId_WorksCorrectly() {
        request.setTutorId(2L);
        assertEquals(2L, request.getTutorId());
    }

    @Test
    @DisplayName("Should set and get amount")
    void setAndGetAmount_WorksCorrectly() {
        BigDecimal amount = BigDecimal.valueOf(250.00);
        request.setAmount(amount);
        assertEquals(amount, request.getAmount());
    }

    @Test
    @DisplayName("Should set and get payment method")
    void setAndGetPaymentMethod_WorksCorrectly() {
        request.setPaymentMethod("PayPal");
        assertEquals("PayPal", request.getPaymentMethod());
    }

    @Test
    @DisplayName("Should set and get notes")
    void setAndGetNotes_WorksCorrectly() {
        request.setNotes("Monthly payout");
        assertEquals("Monthly payout", request.getNotes());
    }
}
