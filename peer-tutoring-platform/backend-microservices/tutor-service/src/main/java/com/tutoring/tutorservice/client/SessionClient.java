package com.tutoring.tutorservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "session-service", url = "http://localhost:8083")
public interface SessionClient {

    @GetMapping("/api/sessions/tutor/{tutorId}")
    List<SessionResponse> getTutorSessions(@PathVariable Long tutorId);

    record SessionResponse(
            Long id,
            Long tutorId,
            String title,
            String description,
            String subject,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal price,
            int maxStudents,
            int enrolledStudents,
            String status,
            boolean full,
            boolean past
    ) {}
}
