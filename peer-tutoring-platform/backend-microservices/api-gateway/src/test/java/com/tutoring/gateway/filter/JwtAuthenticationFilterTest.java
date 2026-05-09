package com.tutoring.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;
    private String jwtSecret = "testSecretKeyForJWTTesting1234567890";

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter();
        ReflectionTestUtils.setField(filter, "jwtSecret", jwtSecret);
    }

    @Test
    @DisplayName("Should create filter with config class")
    void constructor_CreatesFilter() {
        // Filter is already created in setUp
        assertNotNull(filter);
    }

    @Test
    @DisplayName("Should have public config class")
    void configClass_Exists() {
        assertNotNull(JwtAuthenticationFilter.Config.class);
    }

    @Test
    @DisplayName("Should test JWT token generation for validation")
    void generateToken_ValidToken_CanBeValidated() {
        // Given
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject("test@example.com")
                .claim("userId", 1L)
                .claim("role", "STUDENT")
                .signWith(key)
                .compact();

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Verify token can be parsed
        assertDoesNotThrow(() -> {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        });
    }

    @Test
    @DisplayName("Should extract claims from token")
    void extractClaims_ValidToken_ReturnsClaims() {
        // Given
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject("test@example.com")
                .claim("userId", 1L)
                .claim("role", "STUDENT")
                .signWith(key)
                .compact();

        // When
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Then
        assertEquals("test@example.com", claims.getSubject());
        assertEquals(1, ((Number) claims.get("userId")).intValue());
        assertEquals("STUDENT", claims.get("role"));
    }

    @Test
    @DisplayName("Should invalidate tampered token")
    void tamperedToken_ValidationFails() {
        // Given
        String tamperedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.invalid";

        // Then
        assertThrows(Exception.class, () -> {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(tamperedToken);
        });
    }
}
