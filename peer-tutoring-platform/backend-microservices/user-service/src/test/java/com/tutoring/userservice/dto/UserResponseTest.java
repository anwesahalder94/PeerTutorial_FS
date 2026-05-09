package com.tutoring.userservice.dto;

import com.tutoring.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setRole(User.Role.STUDENT);
        user.setActive(true);
        user.setVerified(false);
    }

    @Test
    @DisplayName("Should create UserResponse from User correctly")
    void constructor_FromUser_CopiesFields() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);

        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("John Doe", response.getFullName());
        assertEquals("1234567890", response.getPhoneNumber());
        assertEquals("STUDENT", response.getRole());
        assertTrue(response.isActive());
        assertFalse(response.isVerified());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle all role types in UserResponse")
    void constructor_WithDifferentRoles_HandlesCorrectly() {
        // Test STUDENT
        user.setRole(User.Role.STUDENT);
        UserResponse studentResponse = new UserResponse(user);
        assertEquals("STUDENT", studentResponse.getRole());

        // Test TUTOR
        user.setRole(User.Role.TUTOR);
        UserResponse tutorResponse = new UserResponse(user);
        assertEquals("TUTOR", tutorResponse.getRole());

        // Test ADMIN
        user.setRole(User.Role.ADMIN);
        UserResponse adminResponse = new UserResponse(user);
        assertEquals("ADMIN", adminResponse.getRole());
    }

    @Test
    @DisplayName("Should get ID")
    void getId_ReturnsId() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Should get email")
    void getEmail_ReturnsEmail() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Should get first name")
    void getFirstName_ReturnsFirstName() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("John", response.getFirstName());
    }

    @Test
    @DisplayName("Should get last name")
    void getLastName_ReturnsLastName() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("Doe", response.getLastName());
    }

    @Test
    @DisplayName("Should get full name")
    void getFullName_ReturnsFullName() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("John Doe", response.getFullName());
    }

    @Test
    @DisplayName("Should get phone number")
    void getPhoneNumber_ReturnsPhoneNumber() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("1234567890", response.getPhoneNumber());
    }

    @Test
    @DisplayName("Should get role")
    void getRole_ReturnsRole() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    @DisplayName("Should get active status")
    void isActive_ReturnsActiveStatus() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertTrue(response.isActive());
    }

    @Test
    @DisplayName("Should get verified status")
    void isVerified_ReturnsVerifiedStatus() {
        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertFalse(response.isVerified());
    }

    @Test
    @DisplayName("Should get created at timestamp")
    void getCreatedAt_ReturnsCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);

        // When
        UserResponse response = new UserResponse(user);

        // Then
        assertEquals(createdAt, response.getCreatedAt());
    }
}
