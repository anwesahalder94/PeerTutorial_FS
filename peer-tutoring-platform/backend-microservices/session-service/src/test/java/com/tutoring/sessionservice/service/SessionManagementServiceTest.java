package com.tutoring.sessionservice.service;

import com.tutoring.sessionservice.dto.SessionRequest;
import com.tutoring.sessionservice.model.Session;
import com.tutoring.sessionservice.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionManagementServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionManagementService sessionService;

    private Session session;
    private SessionRequest sessionRequest;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setTutorId(1L);
        session.setTitle("Math Session");
        session.setDescription("Learn Algebra");
        session.setSubject("Mathematics");
        session.setStartTime(LocalDateTime.now().plusDays(1));
        session.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        session.setPrice(BigDecimal.valueOf(50.00));
        session.setMaxStudents(5);
        session.setEnrolledStudents(0);
        session.setStatus(Session.SessionStatus.AVAILABLE);

        sessionRequest = new SessionRequest();
        sessionRequest.setTitle("Math Session");
        sessionRequest.setDescription("Learn Algebra");
        sessionRequest.setSubject("Mathematics");
        sessionRequest.setStartTime(LocalDateTime.now().plusDays(1));
        sessionRequest.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        sessionRequest.setPrice(BigDecimal.valueOf(50.00));
        sessionRequest.setMaxStudents(5);
    }

    @Test
    @DisplayName("Should create session successfully")
    void createSession_ValidRequest_ReturnsSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.createSession(1L, sessionRequest);

        assertNotNull(result);
        assertEquals("Math Session", result.getTitle());
        assertEquals(Session.SessionStatus.AVAILABLE, result.getStatus());
    }

    @Test
    @DisplayName("Should get session by ID")
    void getSessionById_ExistingSession_ReturnsSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getSessionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Should throw exception when session not found")
    void getSessionById_NonExistingSession_ThrowsException() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sessionService.getSessionById(1L));
        assertEquals("Session not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get all sessions")
    void getAllSessions_ReturnsAllSessions() {
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));

        List<Session> result = sessionService.getAllSessions();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get available sessions")
    void getAvailableSessions_ReturnsAvailableSessions() {
        when(sessionRepository.findByStatus(Session.SessionStatus.AVAILABLE)).thenReturn(Arrays.asList(session));

        List<Session> result = sessionService.getAvailableSessions();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get tutor sessions")
    void getTutorSessions_ReturnsTutorSessions() {
        when(sessionRepository.findByTutorId(1L)).thenReturn(Arrays.asList(session));

        List<Session> result = sessionService.getTutorSessions(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get sessions by subject")
    void getSessionsBySubject_ReturnsMatchingSessions() {
        when(sessionRepository.findBySubjectContainingIgnoreCase("Math")).thenReturn(Arrays.asList(session));

        List<Session> result = sessionService.getSessionsBySubject("Math");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should update session successfully")
    void updateSession_ValidRequest_ReturnsUpdatedSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionRequest.setTitle("Updated Title");
        Session result = sessionService.updateSession(1L, 1L, sessionRequest);

        assertNotNull(result);
        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("Should throw exception when updating with wrong tutor")
    void updateSession_WrongTutor_ThrowsException() {
        session.setTutorId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sessionService.updateSession(1L, 2L, sessionRequest));
        assertEquals("Not authorized to update this session", exception.getMessage());
    }

    @Test
    @DisplayName("Should cancel session successfully")
    void cancelSession_ValidRequest_CancelsSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.cancelSession(1L, 1L);

        assertEquals(Session.SessionStatus.CANCELLED, session.getStatus());
    }

    @Test
    @DisplayName("Should delete session successfully")
    void deleteSession_ValidRequest_DeletesSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        doNothing().when(sessionRepository).delete(any(Session.class));

        sessionService.deleteSession(1L, 1L);

        verify(sessionRepository).delete(session);
    }

    @Test
    @DisplayName("Should increment enrollment successfully")
    void incrementEnrollment_ValidSession_IncrementsEnrollment() {
        session.setMaxStudents(5);
        session.setEnrolledStudents(0);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.incrementEnrollment(1L);

        assertEquals(1, session.getEnrolledStudents());
    }

    @Test
    @DisplayName("Should change status to BOOKED when full")
    void incrementEnrollment_WhenFull_ChangesStatus() {
        session.setMaxStudents(1);
        session.setEnrolledStudents(0);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.incrementEnrollment(1L);

        assertEquals(Session.SessionStatus.BOOKED, session.getStatus());
    }

    @Test
    @DisplayName("Should decrement enrollment successfully")
    void decrementEnrollment_ValidSession_DecrementsEnrollment() {
        session.setMaxStudents(5);
        session.setEnrolledStudents(2);
        session.setStatus(Session.SessionStatus.BOOKED);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.decrementEnrollment(1L);

        assertEquals(1, session.getEnrolledStudents());
    }

    @Test
    @DisplayName("Should change status to AVAILABLE when not full")
    void decrementEnrollment_WhenNotFull_ChangesStatus() {
        session.setMaxStudents(5);
        session.setEnrolledStudents(5);
        session.setStatus(Session.SessionStatus.BOOKED);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.decrementEnrollment(1L);

        assertEquals(Session.SessionStatus.AVAILABLE, session.getStatus());
    }

    @Test
    @DisplayName("Should not decrement below zero")
    void decrementEnrollment_ZeroEnrollment_StaysAtZero() {
        session.setEnrolledStudents(0);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.decrementEnrollment(1L);

        assertEquals(0, session.getEnrolledStudents());
    }
}
