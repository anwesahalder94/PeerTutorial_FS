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
@RequestMapping("/api/tutors")
@CrossOrigin(origins = "*")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping("/profile")
    public ResponseEntity<TutorProfile> createProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TutorProfileRequest request) {
        return ResponseEntity.ok(tutorService.createProfile(userId, request));
    }

    @PutMapping("/profile")
    public ResponseEntity<TutorProfile> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TutorProfileRequest request) {
        return ResponseEntity.ok(tutorService.updateProfile(userId, request));
    }

    @GetMapping("/profile")
    public ResponseEntity<TutorProfile> getMyProfile(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(tutorService.getProfileByUserId(userId));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<TutorProfile> getProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tutorService.getProfileByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<TutorProfileResponse>> getAllApprovedTutors() {
        return ResponseEntity.ok(tutorService.getAllApprovedProfilesWithUser());
    }

    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(
            @RequestHeader("X-User-Id") Long studentId,
            @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(tutorService.createRating(studentId, request));
    }

    @GetMapping("/{tutorId}/ratings")
    public ResponseEntity<List<Rating>> getTutorRatings(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorService.getTutorRatings(tutorId));
    }
}
