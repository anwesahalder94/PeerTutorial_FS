package com.tutoring.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

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

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            if (!hasAuthHeader(request)) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = extractToken(request);
            if (token == null || !isValidToken(token)) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            Claims claims = extractClaims(token);
            Object userId = claims.get("userId");
            Object role = claims.get("role");
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId != null ? userId.toString() : null)
                    .header("X-User-Role", role != null ? role.toString() : null)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean hasAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {
    }
}
