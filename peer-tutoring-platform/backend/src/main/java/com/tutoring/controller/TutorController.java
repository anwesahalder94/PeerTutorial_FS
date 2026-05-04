package com.tutoring.controller;

import com.tutoring.dto.RatingRequest;
import com.tutoring.dto.TutorProfileRequest;
import com.tutoring.model.Rating;
import com.tutoring.model.TutorProfile;
import com.tutoring.security.UserDetailsImpl;
import com.tutoring.service.RatingService;
import com.tutoring.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/tutors")
    public ResponseEntity<List<TutorProfile>> getAllApprovedTutors() {
        return ResponseEntity.ok(tutorService.getAllApprovedTutors());
    }

    @GetMapping("/tutors/search")
    public ResponseEntity<List<TutorProfile>> searchTutors(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Double minRating) {
        return ResponseEntity.ok(tutorService.searchTutors(subject, minRating));
    }

    @GetMapping("/tutors/{id}")
    public ResponseEntity<?> getTutorById(@PathVariable Long id) {
        TutorProfile profile = tutorService.getProfileByUserId(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/tutor/profile")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody TutorProfileRequest request) {
        TutorProfile profile = tutorService.createProfile(userDetails.getId(), request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/tutor/profile")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        TutorProfile profile = tutorService.getProfileByUserId(userDetails.getId());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/tutor/profile")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody TutorProfileRequest request) {
        TutorProfile profile = tutorService.updateProfile(userDetails.getId(), request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/tutors/{tutorId}/ratings")
    public ResponseEntity<List<Rating>> getTutorRatings(@PathVariable Long tutorId) {
        return ResponseEntity.ok(ratingService.getTutorRatings(tutorId));
    }

    @PostMapping("/student/ratings")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> createRating(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody RatingRequest request) {
        Rating rating = ratingService.createRating(userDetails.getId(), request);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/student/ratings")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Rating>> getMyRatings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ratingService.getStudentRatings(userDetails.getId()));
    }
}