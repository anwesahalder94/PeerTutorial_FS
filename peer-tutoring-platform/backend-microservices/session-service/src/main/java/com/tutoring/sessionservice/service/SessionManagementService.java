package com.tutoring.sessionservice.service;

import com.tutoring.sessionservice.dto.SessionRequest;
import com.tutoring.sessionservice.model.Session;
import com.tutoring.sessionservice.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SessionManagementService {

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public Session createSession(Long tutorId, SessionRequest request) {
        Session session = new Session();
        session.setTutorId(tutorId);
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSubject(request.getSubject());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setPrice(request.getPrice());
        session.setMaxStudents(request.getMaxStudents());
        session.setStatus(Session.SessionStatus.AVAILABLE);
        return sessionRepository.save(session);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getAvailableSessions() {
        return sessionRepository.findByStatus(Session.SessionStatus.AVAILABLE);
    }

    public List<Session> getTutorSessions(Long tutorId) {
        return sessionRepository.findByTutorId(tutorId);
    }

    public List<Session> getSessionsBySubject(String subject) {
        return sessionRepository.findBySubjectContainingIgnoreCase(subject);
    }

    @Transactional
    public Session updateSession(Long sessionId, Long tutorId, SessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getTutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to update this session");
        }

        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSubject(request.getSubject());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setPrice(request.getPrice());
        session.setMaxStudents(request.getMaxStudents());

        return sessionRepository.save(session);
    }

    @Transactional
    public void cancelSession(Long sessionId, Long tutorId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getTutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to cancel this session");
        }

        session.setStatus(Session.SessionStatus.CANCELLED);
        sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long sessionId, Long tutorId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getTutorId().equals(tutorId)) {
            throw new RuntimeException("Not authorized to delete this session");
        }

        sessionRepository.delete(session);
    }

    // Called by Booking Service via Feign/REST
    @Transactional
    public void incrementEnrollment(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setEnrolledStudents(session.getEnrolledStudents() + 1);
        if (session.isFull()) {
            session.setStatus(Session.SessionStatus.BOOKED);
        }
        sessionRepository.save(session);
    }

    @Transactional
    public void decrementEnrollment(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setEnrolledStudents(Math.max(0, session.getEnrolledStudents() - 1));
        if (!session.isFull() && session.getStatus() == Session.SessionStatus.BOOKED) {
            session.setStatus(Session.SessionStatus.AVAILABLE);
        }
        sessionRepository.save(session);
    }
}
