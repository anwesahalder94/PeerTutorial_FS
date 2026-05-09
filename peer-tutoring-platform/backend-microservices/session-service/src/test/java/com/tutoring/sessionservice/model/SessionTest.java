package com.tutoring.sessionservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = new Session();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        session.setId(1L);
        assertEquals(1L, session.getId());
    }

    @Test
    @DisplayName("Should set and get tutor ID")
    void setTutorIdAndGetTutorId_WorksCorrectly() {
        session.setTutorId(1L);
        assertEquals(1L, session.getTutorId());
    }

    @Test
    @DisplayName("Should set and get title")
    void setTitleAndGetTitle_WorksCorrectly() {
        session.setTitle("Math Session");
        assertEquals("Math Session", session.getTitle());
    }

    @Test
    @DisplayName("Should set and get description")
    void setDescriptionAndGetDescription_WorksCorrectly() {
        session.setDescription("Learn algebra");
        assertEquals("Learn algebra", session.getDescription());
    }

    @Test
    @DisplayName("Should set and get subject")
    void setSubjectAndGetSubject_WorksCorrectly() {
        session.setSubject("Mathematics");
        assertEquals("Mathematics", session.getSubject());
    }

    @Test
    @DisplayName("Should set and get start time")
    void setStartTimeAndGetStartTime_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        session.setStartTime(now);
        assertEquals(now, session.getStartTime());
    }

    @Test
    @DisplayName("Should set and get end time")
    void setEndTimeAndGetEndTime_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        session.setEndTime(now);
        assertEquals(now, session.getEndTime());
    }

    @Test
    @DisplayName("Should set and get price")
    void setPriceAndGetPrice_WorksCorrectly() {
        BigDecimal price = BigDecimal.valueOf(50.00);
        session.setPrice(price);
        assertEquals(price, session.getPrice());
    }

    @Test
    @DisplayName("Should set and get max students")
    void setMaxStudentsAndGetMaxStudents_WorksCorrectly() {
        session.setMaxStudents(10);
        assertEquals(10, session.getMaxStudents());
    }

    @Test
    @DisplayName("Should set and get enrolled students")
    void setEnrolledStudentsAndGetEnrolledStudents_WorksCorrectly() {
        session.setEnrolledStudents(5);
        assertEquals(5, session.getEnrolledStudents());
    }

    @Test
    @DisplayName("Should set and get status")
    void setStatusAndGetStatus_WorksCorrectly() {
        session.setStatus(Session.SessionStatus.BOOKED);
        assertEquals(Session.SessionStatus.BOOKED, session.getStatus());
    }

    @Test
    @DisplayName("Should have default AVAILABLE status")
    void defaultStatus_IsAvailable() {
        Session newSession = new Session();
        assertEquals(Session.SessionStatus.AVAILABLE, newSession.getStatus());
    }

    @Test
    @DisplayName("Should detect when session is full")
    void isFull_WhenFull_ReturnsTrue() {
        session.setMaxStudents(5);
        session.setEnrolledStudents(5);
        assertTrue(session.isFull());
    }

    @Test
    @DisplayName("Should detect when session is not full")
    void isFull_WhenNotFull_ReturnsFalse() {
        session.setMaxStudents(5);
        session.setEnrolledStudents(3);
        assertFalse(session.isFull());
    }

    @Test
    @DisplayName("Should detect past session")
    void isPast_WhenPast_ReturnsTrue() {
        session.setStartTime(LocalDateTime.now().minusDays(1));
        assertTrue(session.isPast());
    }

    @Test
    @DisplayName("Should detect future session")
    void isPast_WhenFuture_ReturnsFalse() {
        session.setStartTime(LocalDateTime.now().plusDays(1));
        assertFalse(session.isPast());
    }

    @Test
    @DisplayName("Should test all session status enum values")
    void sessionStatusEnum_HasAllValues() {
        assertEquals(4, Session.SessionStatus.values().length);
        assertNotNull(Session.SessionStatus.AVAILABLE);
        assertNotNull(Session.SessionStatus.BOOKED);
        assertNotNull(Session.SessionStatus.CANCELLED);
        assertNotNull(Session.SessionStatus.COMPLETED);
    }
}
