package com.tutoring.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GlobalJwtFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalJwtFilter.class);

    @Value("${jwt.secret:peerTutoringSecretKey2026PeerTutoringPlatformBITSAssignment}")
    private String jwtSecret;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/oauth2",
            "/api/public",
            "/actuator",
            "/api/sessions/available"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip public paths
        if (isPublicPath(path)) {
            logger.info("GlobalJwtFilter: Skipping public path {}", path);
            return chain.filter(exchange);
        }

        logger.info("GlobalJwtFilter: Processing path {}", path);

        // Check for Authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        logger.info("GlobalJwtFilter: Auth header present: {}", authHeader != null);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

                Object userId = claims.get("userId");
                Object role = claims.get("role");

                if (userId != null) {
                    logger.info("GlobalJwtFilter: Adding X-User-Id header: {}", userId);
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", userId.toString())
                            .header("X-User-Role", role != null ? role.toString() : "")
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
            } catch (Exception e) {
                // Invalid token, continue without headers
            }
        }

        return chain.filter(exchange);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return -100; // High priority
    }
}
