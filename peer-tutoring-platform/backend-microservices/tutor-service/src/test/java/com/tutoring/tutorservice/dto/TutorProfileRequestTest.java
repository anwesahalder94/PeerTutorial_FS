package com.tutoring.tutorservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TutorProfileRequestTest {

    private Validator validator;
    private TutorProfileRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        request = new TutorProfileRequest();
        request.setBio("Experienced math tutor");
        request.setSubject("Mathematics");
        request.setSubjects(Arrays.asList("Algebra", "Calculus"));
        request.setHourlyRate(BigDecimal.valueOf(50.00));
        request.setYearsOfExperience(5);
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validRequest_NoViolations() {
        Set<ConstraintViolation<TutorProfileRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when bio is blank")
    void blankBio_HasViolation() {
        request.setBio("");
        Set<ConstraintViolation<TutorProfileRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when subject is blank")
    void blankSubject_HasViolation() {
        request.setSubject("");
        Set<ConstraintViolation<TutorProfileRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when hourly rate is null")
    void nullHourlyRate_HasViolation() {
        request.setHourlyRate(null);
        Set<ConstraintViolation<TutorProfileRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get bio")
    void setAndGetBio_WorksCorrectly() {
        request.setBio("New bio");
        assertEquals("New bio", request.getBio());
    }

    @Test
    @DisplayName("Should set and get subject")
    void setAndGetSubject_WorksCorrectly() {
        request.setSubject("Physics");
        assertEquals("Physics", request.getSubject());
    }

    @Test
    @DisplayName("Should set and get subjects list")
    void setAndGetSubjects_WorksCorrectly() {
        request.setSubjects(Arrays.asList("Geometry"));
        assertEquals(1, request.getSubjects().size());
    }

    @Test
    @DisplayName("Should set and get hourly rate")
    void setAndGetHourlyRate_WorksCorrectly() {
        BigDecimal rate = BigDecimal.valueOf(75.00);
        request.setHourlyRate(rate);
        assertEquals(rate, request.getHourlyRate());
    }

    @Test
    @DisplayName("Should set and get years of experience")
    void setAndGetYearsOfExperience_WorksCorrectly() {
        request.setYearsOfExperience(10);
        assertEquals(10, request.getYearsOfExperience());
    }
}
