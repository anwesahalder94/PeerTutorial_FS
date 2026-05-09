package com.tutoring.userservice.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse("token123", 1L, "test@example.com", "John", "Doe", "STUDENT");
    }

    @Test
    @DisplayName("Should create JwtResponse with all fields")
    void constructor_SetsAllFields() {
        // Then
        assertEquals("token123", jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("test@example.com", jwtResponse.getEmail());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertEquals("STUDENT", jwtResponse.getRole());
    }

    @Test
    @DisplayName("Should set and get token")
    void setAndGetToken_WorksCorrectly() {
        // When
        jwtResponse.setToken("newToken");

        // Then
        assertEquals("newToken", jwtResponse.getToken());
    }

    @Test
    @DisplayName("Should set and get type")
    void setAndGetType_WorksCorrectly() {
        // When
        jwtResponse.setType("Basic");

        // Then
        assertEquals("Basic", jwtResponse.getType());
    }

    @Test
    @DisplayName("Should set and get id")
    void setAndGetId_WorksCorrectly() {
        // When
        jwtResponse.setId(2L);

        // Then
        assertEquals(2L, jwtResponse.getId());
    }

    @Test
    @DisplayName("Should set and get email")
    void setAndGetEmail_WorksCorrectly() {
        // When
        jwtResponse.setEmail("new@example.com");

        // Then
        assertEquals("new@example.com", jwtResponse.getEmail());
    }

    @Test
    @DisplayName("Should set and get first name")
    void setAndGetFirstName_WorksCorrectly() {
        // When
        jwtResponse.setFirstName("Jane");

        // Then
        assertEquals("Jane", jwtResponse.getFirstName());
    }

    @Test
    @DisplayName("Should set and get last name")
    void setAndGetLastName_WorksCorrectly() {
        // When
        jwtResponse.setLastName("Smith");

        // Then
        assertEquals("Smith", jwtResponse.getLastName());
    }

    @Test
    @DisplayName("Should set and get role")
    void setAndGetRole_WorksCorrectly() {
        // When
        jwtResponse.setRole("TUTOR");

        // Then
        assertEquals("TUTOR", jwtResponse.getRole());
    }

    @Test
    @DisplayName("Should handle different role types")
    void handleDifferentRoles() {
        // Test STUDENT
        jwtResponse.setRole("STUDENT");
        assertEquals("STUDENT", jwtResponse.getRole());

        // Test TUTOR
        jwtResponse.setRole("TUTOR");
        assertEquals("TUTOR", jwtResponse.getRole());

        // Test ADMIN
        jwtResponse.setRole("ADMIN");
        assertEquals("ADMIN", jwtResponse.getRole());
    }

    @Test
    @DisplayName("Should handle null values")
    void handleNullValues() {
        // When
        jwtResponse.setToken(null);
        jwtResponse.setEmail(null);
        jwtResponse.setFirstName(null);
        jwtResponse.setLastName(null);
        jwtResponse.setRole(null);

        // Then
        assertNull(jwtResponse.getToken());
        assertNull(jwtResponse.getEmail());
        assertNull(jwtResponse.getFirstName());
        assertNull(jwtResponse.getLastName());
        assertNull(jwtResponse.getRole());
    }

    @Test
    @DisplayName("Should maintain default Bearer type")
    void defaultType_IsBearer() {
        // When
        JwtResponse newResponse = new JwtResponse("token", 1L, "email", "first", "last", "role");

        // Then
        assertEquals("Bearer", newResponse.getType());
    }
}
