package com.tutoring.controller;

import com.tutoring.dto.MessageResponse;
import com.tutoring.dto.PayoutRequest;
import com.tutoring.model.*;
import com.tutoring.security.UserDetailsImpl;
import com.tutoring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", adminService.getTotalUsers());
        stats.put("totalTutors", adminService.getTotalTutors());
        stats.put("totalBookings", adminService.getTotalBookings());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
    }

    @GetMapping("/tutors/pending")
    public ResponseEntity<List<TutorProfile>> getPendingTutors() {
        return ResponseEntity.ok(adminService.getPendingTutorProfiles());
    }

    @PostMapping("/tutors/{profileId}/approve")
    public ResponseEntity<?> approveTutor(@PathVariable Long profileId) {
        TutorProfile profile = adminService.approveTutor(profileId);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/tutors/{profileId}/reject")
    public ResponseEntity<?> rejectTutor(@PathVariable Long profileId) {
        TutorProfile profile = adminService.rejectTutor(profileId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/payouts")
    public ResponseEntity<List<Payout>> getAllPayouts() {
        return ResponseEntity.ok(adminService.getAllPayouts());
    }

    @PostMapping("/payouts")
    public ResponseEntity<?> createPayout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody PayoutRequest request) {
        Payout payout = adminService.createPayout(userDetails.getId(), request);
        return ResponseEntity.ok(payout);
    }

    @PostMapping("/payouts/{payoutId}/process")
    public ResponseEntity<?> processPayout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable Long payoutId) {
        Payout payout = adminService.processPayout(payoutId, userDetails.getId());
        return ResponseEntity.ok(payout);
    }
}