package com.tutoring.gateway.config;

import com.tutoring.gateway.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatewayConfigTest {

    @Test
    @DisplayName("Should create filter")
    void filter_CanBeCreated() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        assertNotNull(filter);
    }

    @Test
    @DisplayName("Should have config class")
    void configClass_Exists() {
        JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
        assertNotNull(config);
    }
}
