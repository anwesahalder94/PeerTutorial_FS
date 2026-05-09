package com.tutoring.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiGatewayApplicationTest {

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Should have main method")
    void mainMethod_Exists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = ApiGatewayApplication.class;
            assertNotNull(clazz.getMethod("main", String[].class));
        });
    }
}
