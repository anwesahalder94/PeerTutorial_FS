package com.tutoring.userservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validLoginRequest_NoViolations() {
        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void blankEmail_HasViolation() {
        // Given
        loginRequest.setEmail("");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void blankPassword_HasViolation() {
        // Given
        loginRequest.setPassword("");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should fail validation when email is null")
    void nullEmail_HasViolation() {
        // Given
        loginRequest.setEmail(null);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when password is null")
    void nullPassword_HasViolation() {
        // Given
        loginRequest.setPassword(null);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get email")
    void setAndGetEmail_WorksCorrectly() {
        // When
        loginRequest.setEmail("new@example.com");

        // Then
        assertEquals("new@example.com", loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void setAndGetPassword_WorksCorrectly() {
        // When
        loginRequest.setPassword("newpassword");

        // Then
        assertEquals("newpassword", loginRequest.getPassword());
    }
}
