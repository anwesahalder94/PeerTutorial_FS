package com.tutoring.controller;

import com.tutoring.dto.MessageResponse;
import com.tutoring.dto.SessionRequest;
import com.tutoring.model.Session;
import com.tutoring.security.UserDetailsImpl;
import com.tutoring.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Session>> getAvailableSessions() {
        return ResponseEntity.ok(sessionService.getAvailableSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Session>> getSessionsByTutor(@PathVariable Long tutorId) {
        return ResponseEntity.ok(sessionService.getSessionsByTutor(tutorId));
    }

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createSession(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @Valid @RequestBody SessionRequest request) {
        Session session = sessionService.createSession(userDetails.getId(), request);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateSession(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long id,
                                           @Valid @RequestBody SessionRequest request) {
        Session session = sessionService.updateSession(id, userDetails.getId(), request);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteSession(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long id) {
        sessionService.deleteSession(id, userDetails.getId());
        return ResponseEntity.ok(new MessageResponse("Session deleted successfully"));
    }
}