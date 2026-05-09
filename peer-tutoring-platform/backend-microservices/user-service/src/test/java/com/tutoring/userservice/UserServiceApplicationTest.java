package com.tutoring.userservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceApplicationTest {

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        // This test verifies that the Spring application context loads correctly
        // If this test passes, it means all beans are properly configured
        assertTrue(true);
    }

    @Test
    @DisplayName("Should have main method")
    void mainMethod_Exists() {
        // Verify the main method exists and can be called
        assertDoesNotThrow(() -> {
            // We can't actually start the server in a test, but we can verify the method exists
            Class<?> clazz = UserServiceApplication.class;
            assertNotNull(clazz.getMethod("main", String[].class));
        });
    }
}
