package com.tutoring.bookingservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestTest {

    private Validator validator;
    private BookingRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        request = new BookingRequest();
        request.setSessionId(1L);
        request.setNotes("Please bring calculator");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validRequest_NoViolations() {
        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when session ID is null")
    void nullSessionId_HasViolation() {
        request.setSessionId(null);
        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get session ID")
    void setAndGetSessionId_WorksCorrectly() {
        request.setSessionId(2L);
        assertEquals(2L, request.getSessionId());
    }

    @Test
    @DisplayName("Should set and get notes")
    void setAndGetNotes_WorksCorrectly() {
        request.setNotes("New notes");
        assertEquals("New notes", request.getNotes());
    }

    @Test
    @DisplayName("Should handle null notes")
    void nullNotes_NoViolation() {
        request.setNotes(null);
        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
