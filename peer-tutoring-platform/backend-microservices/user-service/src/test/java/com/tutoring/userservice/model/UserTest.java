package com.tutoring.userservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with default values")
    void user_DefaultValues_HasDefaults() {
        // Then
        assertTrue(user.isActive());
        assertFalse(user.isVerified());
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        // When
        user.setId(1L);

        // Then
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Should set and get email")
    void setEmailAndGetEmail_WorksCorrectly() {
        // When
        user.setEmail("test@example.com");

        // Then
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void setPasswordAndGetPassword_WorksCorrectly() {
        // When
        user.setPassword("securePassword");

        // Then
        assertEquals("securePassword", user.getPassword());
    }

    @Test
    @DisplayName("Should set and get first name")
    void setFirstNameAndGetFirstName_WorksCorrectly() {
        // When
        user.setFirstName("John");

        // Then
        assertEquals("John", user.getFirstName());
    }

    @Test
    @DisplayName("Should set and get last name")
    void setLastNameAndGetLastName_WorksCorrectly() {
        // When
        user.setLastName("Doe");

        // Then
        assertEquals("Doe", user.getLastName());
    }

    @Test
    @DisplayName("Should get full name correctly")
    void getFullName_WithFirstAndLastName_ReturnsFullName() {
        // Given
        user.setFirstName("John");
        user.setLastName("Doe");

        // Then
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    @DisplayName("Should handle full name with empty strings")
    void getFullName_WithEmptyStrings_ReturnsSpace() {
        // Given
        user.setFirstName("");
        user.setLastName("");

        // Then
        assertEquals(" ", user.getFullName());
    }

    @Test
    @DisplayName("Should set and get phone number")
    void setPhoneNumberAndGetPhoneNumber_WorksCorrectly() {
        // When
        user.setPhoneNumber("1234567890");

        // Then
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    @DisplayName("Should set and get role")
    void setRoleAndGetRole_WorksCorrectly() {
        // When
        user.setRole(User.Role.TUTOR);

        // Then
        assertEquals(User.Role.TUTOR, user.getRole());
    }

    @Test
    @DisplayName("Should set and get active status")
    void setActiveAndIsActive_WorksCorrectly() {
        // When
        user.setActive(false);

        // Then
        assertFalse(user.isActive());
    }

    @Test
    @DisplayName("Should set and get verified status")
    void setVerifiedAndIsVerified_WorksCorrectly() {
        // When
        user.setVerified(true);

        // Then
        assertTrue(user.isVerified());
    }

    @Test
    @DisplayName("Should set and get created at timestamp")
    void setCreatedAtAndGetCreatedAt_WorksCorrectly() {
        // Given
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // When
        user.setCreatedAt(now);

        // Then
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updated at timestamp")
    void setUpdatedAtAndGetUpdatedAt_WorksCorrectly() {
        // Given
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // When
        user.setUpdatedAt(now);

        // Then
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test all role enum values")
    void roleEnum_HasAllValues() {
        // Then
        assertEquals(3, User.Role.values().length);
        assertNotNull(User.Role.STUDENT);
        assertNotNull(User.Role.TUTOR);
        assertNotNull(User.Role.ADMIN);
    }
}
