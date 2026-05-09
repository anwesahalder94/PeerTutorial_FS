package com.tutoring.bookingservice.controller;

import com.tutoring.bookingservice.dto.BookingRequest;
import com.tutoring.bookingservice.model.Booking;
import com.tutoring.bookingservice.service.BookingManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingManagementService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestHeader("X-User-Id") Long studentId,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(studentId, request));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(
            @RequestHeader("X-User-Id") Long studentId) {
        return ResponseEntity.ok(bookingService.getStudentBookings(studentId));
    }

    @GetMapping("/tutor-bookings")
    public ResponseEntity<List<Booking>> getTutorBookings(
            @RequestHeader("X-User-Id") Long tutorId) {
        return ResponseEntity.ok(bookingService.getTutorBookings(tutorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Booking>> getSessionBookings(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookingService.getSessionBookings(sessionId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long studentId) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, studentId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId) {
        return ResponseEntity.ok(bookingService.confirmBooking(id, tutorId));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Booking> rejectBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId) {
        return ResponseEntity.ok(bookingService.rejectBooking(id, tutorId));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Booking> completeBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long tutorId) {
        return ResponseEntity.ok(bookingService.completeBooking(id, tutorId));
    }
}
