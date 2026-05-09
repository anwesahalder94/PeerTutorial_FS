package com.tutoring.userservice.dto;

import com.tutoring.userservice.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPhoneNumber("1234567890");
        signupRequest.setRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validSignupRequest_NoViolations() {
        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void blankEmail_HasViolation() {
        // Given
        signupRequest.setEmail("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when email format is invalid")
    void invalidEmail_HasViolation() {
        // Given
        signupRequest.setEmail("invalid-email");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when password is too short")
    void shortPassword_HasViolation() {
        // Given
        signupRequest.setPassword("12345");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void blankPassword_HasViolation() {
        // Given
        signupRequest.setPassword("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when first name is blank")
    void blankFirstName_HasViolation() {
        // Given
        signupRequest.setFirstName("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when last name is blank")
    void blankLastName_HasViolation() {
        // Given
        signupRequest.setLastName("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when phone number is blank")
    void blankPhoneNumber_HasViolation() {
        // Given
        signupRequest.setPhoneNumber("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when role is null")
    void nullRole_HasViolation() {
        // Given
        signupRequest.setRole(null);

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when password exceeds max length")
    void passwordTooLong_HasViolation() {
        // Given
        signupRequest.setPassword("a".repeat(41));

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept password at minimum length")
    void passwordAtMinimumLength_NoViolation() {
        // Given
        signupRequest.setPassword("123456");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept password at maximum length")
    void passwordAtMaximumLength_NoViolation() {
        // Given
        signupRequest.setPassword("a".repeat(40));

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should set and get email")
    void setAndGetEmail_WorksCorrectly() {
        // When
        signupRequest.setEmail("new@example.com");

        // Then
        assertEquals("new@example.com", signupRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void setAndGetPassword_WorksCorrectly() {
        // When
        signupRequest.setPassword("newpassword");

        // Then
        assertEquals("newpassword", signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should set and get first name")
    void setAndGetFirstName_WorksCorrectly() {
        // When
        signupRequest.setFirstName("Jane");

        // Then
        assertEquals("Jane", signupRequest.getFirstName());
    }

    @Test
    @DisplayName("Should set and get last name")
    void setAndGetLastName_WorksCorrectly() {
        // When
        signupRequest.setLastName("Smith");

        // Then
        assertEquals("Smith", signupRequest.getLastName());
    }

    @Test
    @DisplayName("Should set and get phone number")
    void setAndGetPhoneNumber_WorksCorrectly() {
        // When
        signupRequest.setPhoneNumber("9876543210");

        // Then
        assertEquals("9876543210", signupRequest.getPhoneNumber());
    }

    @Test
    @DisplayName("Should set and get role")
    void setAndGetRole_WorksCorrectly() {
        // When
        signupRequest.setRole(User.Role.TUTOR);

        // Then
        assertEquals(User.Role.TUTOR, signupRequest.getRole());
    }
}
