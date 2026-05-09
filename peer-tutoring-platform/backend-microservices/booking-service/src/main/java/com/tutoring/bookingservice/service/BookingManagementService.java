package com.tutoring.bookingservice.service;

import com.tutoring.bookingservice.client.SessionClient;
import com.tutoring.bookingservice.dto.BookingRequest;
import com.tutoring.bookingservice.model.Booking;
import com.tutoring.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingManagementService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SessionClient sessionClient;

    @Transactional
    public Booking createBooking(Long studentId, BookingRequest request) {
        SessionClient.SessionResponse session = sessionClient.getSessionById(request.getSessionId());

        if (session == null) {
            throw new RuntimeException("Session not found");
        }

        if (session.full()) {
            throw new RuntimeException("Session is already full");
        }

        if (session.past()) {
            throw new RuntimeException("Cannot book past sessions");
        }

        if (!"AVAILABLE".equals(session.status()) && !"BOOKED".equals(session.status())) {
            throw new RuntimeException("Session is not available for booking");
        }

        Booking booking = new Booking();
        booking.setStudentId(studentId);
        booking.setSessionId(request.getSessionId());
        booking.setAmount(session.price());
        booking.setNotes(request.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public List<Booking> getStudentBookings(Long studentId) {
        return bookingRepository.findByStudentId(studentId);
    }

    public List<Booking> getTutorBookings(Long tutorId) {
        // Get all sessions for this tutor
        List<SessionClient.SessionResponse> sessions = sessionClient.getTutorSessions(tutorId);
        List<Long> sessionIds = sessions.stream()
                .map(SessionClient.SessionResponse::id)
                .toList();

        if (sessionIds.isEmpty()) {
            return List.of();
        }

        return bookingRepository.findBySessionIdIn(sessionIds);
    }

    public List<Booking> getSessionBookings(Long sessionId) {
        return bookingRepository.findBySessionId(sessionId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Transactional
    public Booking confirmBooking(Long bookingId, Long tutorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        SessionClient.SessionResponse session = sessionClient.getSessionById(booking.getSessionId());

        if (!session.tutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to confirm this booking");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setPaymentStatus(Booking.PaymentStatus.HELD);

        // Update session enrollment
        sessionClient.incrementEnrollment(booking.getSessionId());

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking rejectBooking(Long bookingId, Long tutorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        SessionClient.SessionResponse session = sessionClient.getSessionById(booking.getSessionId());

        if (!session.tutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to reject this booking");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeBooking(Long bookingId, Long tutorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        SessionClient.SessionResponse session = sessionClient.getSessionById(booking.getSessionId());

        if (!session.tutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to complete this booking");
        }

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking must be confirmed first");
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());
        booking.setPaymentStatus(Booking.PaymentStatus.RELEASED_TO_TUTOR);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, Long studentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getStudentId().equals(studentId)) {
            throw new RuntimeException("Not authorized to cancel this booking");
        }

        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed booking");
        }

        if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
            // Decrement session enrollment
            sessionClient.decrementEnrollment(booking.getSessionId());
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);

        return bookingRepository.save(booking);
    }
}
