package com.tutoring.sessionservice.controller;

import com.tutoring.sessionservice.dto.SessionRequest;
import com.tutoring.sessionservice.model.Session;
import com.tutoring.sessionservice.service.SessionManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionManagementService sessionService;

    @PostMapping
    public ResponseEntity<Session> createSession(
            @RequestHeader("X-User-Id") Long tutorId,
            @Valid @RequestBody SessionRequest request) {
        return ResponseEntity.ok(sessionService.createSession(tutorId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Session>> getAvailableSessions() {
        return ResponseEntity.ok(sessionService.getAvailableSessions());
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<Session>> getMySessions(
            @RequestHeader("X-User-Id") Long tutorId) {
        return ResponseEntity.ok(sessionService.getTutorSessions(tutorId));
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Session>> getTutorSessions(@PathVariable Long tutorId) {
        return ResponseEntity.ok(sessionService.getTutorSessions(tutorId));
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<Session>> getSessionsBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(sessionService.getSessionsBySubject(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId,
            @Valid @RequestBody SessionRequest request) {
        return ResponseEntity.ok(sessionService.updateSession(id, tutorId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId) {
        sessionService.deleteSession(id, tutorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSession(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId) {
        sessionService.cancelSession(id, tutorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/increment-enrollment")
    public ResponseEntity<Void> incrementEnrollment(@PathVariable Long id) {
        sessionService.incrementEnrollment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decrement-enrollment")
    public ResponseEntity<Void> decrementEnrollment(@PathVariable Long id) {
        sessionService.decrementEnrollment(id);
        return ResponseEntity.ok().build();
    }
}
