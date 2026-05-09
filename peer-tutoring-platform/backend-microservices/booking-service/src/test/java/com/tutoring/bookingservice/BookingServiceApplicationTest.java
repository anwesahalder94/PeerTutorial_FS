package com.tutoring.bookingservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceApplicationTest {

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Should have main method")
    void mainMethod_Exists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = BookingServiceApplication.class;
            assertNotNull(clazz.getMethod("main", String[].class));
        });
    }
}
