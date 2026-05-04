package com.tutoring.service;

import com.tutoring.dto.SessionRequest;
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
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Session createSession(Long tutorId, SessionRequest request) {
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        List<Session> overlapping = sessionRepository.findOverlappingSessions(
                tutorId, request.getStartTime(), request.getEndTime());

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Time slot conflicts with existing session");
        }

        Session session = new Session();
        session.setTutor(tutor);
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSubject(request.getSubject());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setPrice(request.getPrice());
        session.setMaxStudents(request.getMaxStudents());
        session.setEnrolledStudents(0);
        session.setType(request.getType());
        session.setStatus(Session.SessionStatus.AVAILABLE);

        return sessionRepository.save(session);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getAvailableSessions() {
        return sessionRepository.findByStatus(Session.SessionStatus.AVAILABLE);
    }

    public List<Session> getSessionsByTutor(Long tutorId) {
        return sessionRepository.findByTutorId(tutorId);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Session updateSession(Long sessionId, Long tutorId, SessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getTutor().getId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to update this session");
        }

        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSubject(request.getSubject());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setPrice(request.getPrice());
        session.setMaxStudents(request.getMaxStudents());
        session.setType(request.getType());

        return sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long sessionId, Long tutorId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getTutor().getId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to delete this session");
        }

        List<Booking> bookings = bookingRepository.findBySessionTutorId(tutorId);
        boolean hasConfirmedBookings = bookings.stream()
                .anyMatch(b -> b.getSession().getId().equals(sessionId) &&
                        b.getStatus() == Booking.BookingStatus.CONFIRMED);

        if (hasConfirmedBookings) {
            throw new RuntimeException("Cannot delete session with confirmed bookings");
        }

        sessionRepository.delete(session);
    }
}