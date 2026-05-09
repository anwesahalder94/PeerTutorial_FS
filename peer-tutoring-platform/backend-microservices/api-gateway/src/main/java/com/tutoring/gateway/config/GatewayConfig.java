package com.tutoring.gateway.config;

import com.tutoring.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/auth/**", "/api/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://user-service"))

                // Tutor Service Routes
                .route("tutor-service", r -> r
                        .path("/api/tutors/**", "/api/ratings/**", "/api/payouts/**", "/api/admin/tutors/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://tutor-service"))

                // Session Service Routes
                .route("session-service", r -> r
                        .path("/api/sessions/**", "/api/admin/sessions/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://session-service"))

                // Booking Service Routes
                .route("booking-service", r -> r
                        .path("/api/bookings/**", "/api/admin/bookings/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://booking-service"))

                .build();
    }
}
