package com.tutoring.tutorservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RatingRequestTest {

    private Validator validator;
    private RatingRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        request = new RatingRequest();
        request.setBookingId(1L);
        request.setRating(5);
        request.setComment("Great session!");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validRequest_NoViolations() {
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when booking ID is null")
    void nullBookingId_HasViolation() {
        request.setBookingId(null);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when rating is null")
    void nullRating_HasViolation() {
        request.setRating(null);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when rating is below minimum")
    void ratingBelowMinimum_HasViolation() {
        request.setRating(0);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when rating is above maximum")
    void ratingAboveMaximum_HasViolation() {
        request.setRating(6);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept minimum rating")
    void ratingAtMinimum_NoViolation() {
        request.setRating(1);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept maximum rating")
    void ratingAtMaximum_NoViolation() {
        request.setRating(5);
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get booking ID")
    void setAndGetBookingId_WorksCorrectly() {
        request.setBookingId(2L);
        assertEquals(2L, request.getBookingId());
    }

    @Test
    @DisplayName("Should set and get rating")
    void setAndGetRating_WorksCorrectly() {
        request.setRating(4);
        assertEquals(4, request.getRating());
    }

    @Test
    @DisplayName("Should set and get comment")
    void setAndGetComment_WorksCorrectly() {
        request.setComment("Excellent tutor!");
        assertEquals("Excellent tutor!", request.getComment());
    }
}
