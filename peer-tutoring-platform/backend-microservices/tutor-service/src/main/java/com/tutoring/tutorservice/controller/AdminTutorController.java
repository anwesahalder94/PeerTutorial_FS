package com.tutoring.tutorservice.controller;

import com.tutoring.tutorservice.dto.*;
import com.tutoring.tutorservice.model.*;
import com.tutoring.tutorservice.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tutors")
@CrossOrigin(origins = "*")
public class AdminTutorController {

    @Autowired
    private TutorService tutorService;

    @GetMapping("/pending")
    public ResponseEntity<List<TutorProfile>> getPendingProfiles() {
        return ResponseEntity.ok(tutorService.getPendingProfiles());
    }

    @PostMapping("/{profileId}/approve")
    public ResponseEntity<TutorProfile> approveProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(tutorService.approveProfile(profileId));
    }

    @PostMapping("/{profileId}/reject")
    public ResponseEntity<TutorProfile> rejectProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(tutorService.rejectProfile(profileId));
    }

    @PostMapping("/payouts")
    public ResponseEntity<Payout> createPayout(
            @RequestHeader("X-User-Id") Long adminId,
            @Valid @RequestBody PayoutRequest request) {
        return ResponseEntity.ok(tutorService.createPayout(request, adminId));
    }

    @GetMapping("/payouts")
    public ResponseEntity<List<Payout>> getAllPayouts() {
        return ResponseEntity.ok(tutorService.getAllPayouts());
    }

    @PostMapping("/payouts/{payoutId}/process")
    public ResponseEntity<Payout> processPayout(
            @RequestHeader("X-User-Id") Long adminId,
            @PathVariable Long payoutId) {
        return ResponseEntity.ok(tutorService.processPayout(payoutId, adminId));
    }
}
