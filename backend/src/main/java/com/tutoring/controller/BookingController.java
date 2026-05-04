package com.tutoring.controller;

import com.tutoring.dto.BookingRequest;
import com.tutoring.model.Booking;
import com.tutoring.security.UserDetailsImpl;
import com.tutoring.service.BookingService;
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
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/student/bookings")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> createBooking(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(userDetails.getId(), request);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/student/bookings")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getStudentBookings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookingService.getStudentBookings(userDetails.getId()));
    }

    @GetMapping("/tutor/bookings")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getTutorBookings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookingService.getTutorBookings(userDetails.getId()));
    }

    @PostMapping("/tutor/bookings/{bookingId}/confirm")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> confirmBooking(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable Long bookingId) {
        Booking booking = bookingService.confirmBooking(bookingId, userDetails.getId());
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/tutor/bookings/{bookingId}/reject")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> rejectBooking(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long bookingId) {
        Booking booking = bookingService.rejectBooking(bookingId, userDetails.getId());
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/tutor/bookings/{bookingId}/complete")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> completeBooking(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long bookingId) {
        Booking booking = bookingService.completeBooking(bookingId, userDetails.getId());
        return ResponseEntity.ok(booking);
    }
}