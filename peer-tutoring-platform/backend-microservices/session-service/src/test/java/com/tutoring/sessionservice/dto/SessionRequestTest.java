package com.tutoring.sessionservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SessionRequestTest {

    private Validator validator;
    private SessionRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        request = new SessionRequest();
        request.setTitle("Math Session");
        request.setDescription("Learn Algebra");
        request.setSubject("Mathematics");
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        request.setPrice(BigDecimal.valueOf(50.00));
        request.setMaxStudents(5);
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validRequest_NoViolations() {
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when title is blank")
    void blankTitle_HasViolation() {
        request.setTitle("");
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when subject is blank")
    void blankSubject_HasViolation() {
        request.setSubject("");
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when start time is null")
    void nullStartTime_HasViolation() {
        request.setStartTime(null);
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when end time is null")
    void nullEndTime_HasViolation() {
        request.setEndTime(null);
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when price is null")
    void nullPrice_HasViolation() {
        request.setPrice(null);
        Set<ConstraintViolation<SessionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get title")
    void setAndGetTitle_WorksCorrectly() {
        request.setTitle("New Title");
        assertEquals("New Title", request.getTitle());
    }

    @Test
    @DisplayName("Should set and get description")
    void setAndGetDescription_WorksCorrectly() {
        request.setDescription("New Description");
        assertEquals("New Description", request.getDescription());
    }

    @Test
    @DisplayName("Should set and get subject")
    void setAndGetSubject_WorksCorrectly() {
        request.setSubject("Physics");
        assertEquals("Physics", request.getSubject());
    }

    @Test
    @DisplayName("Should set and get max students")
    void setAndGetMaxStudents_WorksCorrectly() {
        request.setMaxStudents(10);
        assertEquals(10, request.getMaxStudents());
    }
}
