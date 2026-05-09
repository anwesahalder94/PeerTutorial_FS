package com.tutoring.tutorservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TutorProfileTest {

    private TutorProfile tutorProfile;

    @BeforeEach
    void setUp() {
        tutorProfile = new TutorProfile();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        tutorProfile.setId(1L);
        assertEquals(1L, tutorProfile.getId());
    }

    @Test
    @DisplayName("Should set and get user ID")
    void setUserIdAndGetUserId_WorksCorrectly() {
        tutorProfile.setUserId(1L);
        assertEquals(1L, tutorProfile.getUserId());
    }

    @Test
    @DisplayName("Should set and get bio")
    void setBioAndGetBio_WorksCorrectly() {
        tutorProfile.setBio("Experienced tutor");
        assertEquals("Experienced tutor", tutorProfile.getBio());
    }

    @Test
    @DisplayName("Should set and get subject")
    void setSubjectAndGetSubject_WorksCorrectly() {
        tutorProfile.setSubject("Mathematics");
        assertEquals("Mathematics", tutorProfile.getSubject());
    }

    @Test
    @DisplayName("Should set and get subjects list")
    void setSubjectsAndGetSubjects_WorksCorrectly() {
        tutorProfile.setSubjects(Arrays.asList("Algebra", "Calculus"));
        assertEquals(2, tutorProfile.getSubjects().size());
        assertEquals("Algebra", tutorProfile.getSubjects().get(0));
    }

    @Test
    @DisplayName("Should set and get hourly rate")
    void setHourlyRateAndGetHourlyRate_WorksCorrectly() {
        BigDecimal rate = BigDecimal.valueOf(50.00);
        tutorProfile.setHourlyRate(rate);
        assertEquals(rate, tutorProfile.getHourlyRate());
    }

    @Test
    @DisplayName("Should set and get years of experience")
    void setYearsOfExperienceAndGetYearsOfExperience_WorksCorrectly() {
        tutorProfile.setYearsOfExperience(5);
        assertEquals(5, tutorProfile.getYearsOfExperience());
    }

    @Test
    @DisplayName("Should set and get average rating")
    void setAverageRatingAndGetAverageRating_WorksCorrectly() {
        tutorProfile.setAverageRating(4.5);
        assertEquals(4.5, tutorProfile.getAverageRating());
    }

    @Test
    @DisplayName("Should set and get total sessions")
    void setTotalSessionsAndGetTotalSessions_WorksCorrectly() {
        tutorProfile.setTotalSessions(10);
        assertEquals(10, tutorProfile.getTotalSessions());
    }

    @Test
    @DisplayName("Should set and get status")
    void setStatusAndGetStatus_WorksCorrectly() {
        tutorProfile.setStatus(TutorProfile.TutorStatus.APPROVED);
        assertEquals(TutorProfile.TutorStatus.APPROVED, tutorProfile.getStatus());
    }

    @Test
    @DisplayName("Should have default PENDING status")
    void defaultStatus_IsPending() {
        TutorProfile newProfile = new TutorProfile();
        assertEquals(TutorProfile.TutorStatus.PENDING, newProfile.getStatus());
    }

    @Test
    @DisplayName("Should test all tutor status enum values")
    void tutorStatusEnum_HasAllValues() {
        assertEquals(4, TutorProfile.TutorStatus.values().length);
        assertNotNull(TutorProfile.TutorStatus.PENDING);
        assertNotNull(TutorProfile.TutorStatus.APPROVED);
        assertNotNull(TutorProfile.TutorStatus.REJECTED);
        assertNotNull(TutorProfile.TutorStatus.SUSPENDED);
    }

    @Test
    @DisplayName("Should get created at timestamp - initially null before persistence")
    void getCreatedAt_InitiallyNull() {
        // @CreationTimestamp only works with JPA persistence
        // In a unit test without persistence context, this will be null
        assertNull(tutorProfile.getCreatedAt());
    }

    @Test
    @DisplayName("Should get updated at timestamp - initially null before persistence")
    void getUpdatedAt_InitiallyNull() {
        // @UpdateTimestamp only works with JPA persistence
        // In a unit test without persistence context, this will be null
        assertNull(tutorProfile.getUpdatedAt());
    }
}
