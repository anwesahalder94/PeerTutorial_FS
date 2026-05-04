package com.tutoring.service;

import com.tutoring.dto.BookingRequest;
import com.tutoring.model.Booking;
import com.tutoring.model.Session;
import com.tutoring.model.User;
import com.tutoring.repository.BookingRepository;
import com.tutoring.repository.SessionRepository;
import com.tutoring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Booking createBooking(Long studentId, BookingRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isFull()) {
            throw new RuntimeException("Session is already full");
        }

        if (session.isPast()) {
            throw new RuntimeException("Cannot book past sessions");
        }

        if (session.getStatus() != Session.SessionStatus.AVAILABLE) {
            throw new RuntimeException("Session is not available for booking");
        }

        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setSession(session);
        booking.setAmount(session.getPrice());
        booking.setNotes(request.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public List<Booking> getStudentBookings(Long studentId) {
        return bookingRepository.findByStudentId(studentId);
    }

    public List<Booking> getTutorBookings(Long tutorId) {
        return bookingRepository.findBySessionTutorId(tutorId);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId, Long tutorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getSession().getTutor().getId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to confirm this booking");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setPaymentStatus(Booking.PaymentStatus.HELD);

        Session session = booking.getSession();
        session.setEnrolledStudents(session.getEnrolledStudents() + 1);

        if (session.isFull()) {
            session.setStatus(Session.SessionStatus.BOOKED);
        }

        sessionRepository.save(session);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking rejectBooking(Long bookingId, Long tutorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getSession().getTutor().getId().equals(tutorId)) {
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

        if (!booking.getSession().getTutor().getId().equals(tutorId)) {
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
}