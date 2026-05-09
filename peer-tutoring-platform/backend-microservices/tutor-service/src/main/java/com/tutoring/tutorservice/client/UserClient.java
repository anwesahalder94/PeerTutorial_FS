package com.tutoring.tutorservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable Long id);

    record UserResponse(
            Long id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            String role,
            boolean active
    ) {}
}
