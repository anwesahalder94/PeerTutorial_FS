package com.tutoring.userservice.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String jwtSecret = "testSecretKeyForJWTTesting1234567890";
    private int jwtExpiration = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", jwtExpiration);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void generateToken_ValidInput_ReturnsToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String role = "STUDENT";

        // When
        String token = jwtUtil.generateToken(userId, email, role);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Should extract claims from valid token")
    void extractClaims_ValidToken_ReturnsClaims() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String role = "TUTOR";
        String token = jwtUtil.generateToken(userId, email, role);

        // When
        Claims claims = jwtUtil.extractClaims(token);

        // Then
        assertNotNull(claims);
        assertEquals(email, claims.getSubject());
        assertEquals(userId, claims.get("userId", Long.class));
        assertEquals(role, claims.get("role", String.class));
    }

    @Test
    @DisplayName("Should validate correct token")
    void validateToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtUtil.generateToken(1L, "test@example.com", "STUDENT");

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate corrupted token")
    void validateToken_CorruptedToken_ReturnsFalse() {
        // Given
        String corruptedToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validateToken(corruptedToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should invalidate empty token")
    void validateToken_EmptyToken_ReturnsFalse() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtUtil.validateToken(emptyToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should invalidate null token")
    void validateToken_NullToken_ReturnsFalse() {
        // When
        boolean isValid = jwtUtil.validateToken(null);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void generateToken_DifferentUsers_ReturnsDifferentTokens() {
        // Given
        String token1 = jwtUtil.generateToken(1L, "user1@example.com", "STUDENT");
        String token2 = jwtUtil.generateToken(2L, "user2@example.com", "TUTOR");

        // Then
        assertNotEquals(token1, token2);

        Claims claims1 = jwtUtil.extractClaims(token1);
        Claims claims2 = jwtUtil.extractClaims(token2);

        assertEquals("user1@example.com", claims1.getSubject());
        assertEquals("user2@example.com", claims2.getSubject());
        assertEquals(1L, claims1.get("userId", Long.class));
        assertEquals(2L, claims2.get("userId", Long.class));
        assertEquals("STUDENT", claims1.get("role", String.class));
        assertEquals("TUTOR", claims2.get("role", String.class));
    }

    @Test
    @DisplayName("Should generate valid tokens for same user")
    void generateToken_SameUser_ReturnsValidTokens() {
        // Given
        String token1 = jwtUtil.generateToken(1L, "test@example.com", "STUDENT");
        String token2 = jwtUtil.generateToken(1L, "test@example.com", "STUDENT");

        // Then - tokens may be the same or different depending on timing
        // but both should be valid
        assertTrue(jwtUtil.validateToken(token1));
        assertTrue(jwtUtil.validateToken(token2));

        // Both tokens should contain the same claims
        var claims1 = jwtUtil.extractClaims(token1);
        var claims2 = jwtUtil.extractClaims(token2);
        assertEquals(claims1.getSubject(), claims2.getSubject());
        assertEquals(claims1.get("userId"), claims2.get("userId"));
        assertEquals(claims1.get("role"), claims2.get("role"));
    }
}
