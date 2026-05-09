package com.tutoring.tutorservice.dto;

import com.tutoring.tutorservice.model.TutorProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TutorProfileResponseTest {

    private TutorProfileResponse response;

    @BeforeEach
    void setUp() {
        response = new TutorProfileResponse();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setAndGetId_WorksCorrectly() {
        response.setId(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Should set and get user ID")
    void setAndGetUserId_WorksCorrectly() {
        response.setUserId(1L);
        assertEquals(1L, response.getUserId());
    }

    @Test
    @DisplayName("Should set and get bio")
    void setAndGetBio_WorksCorrectly() {
        response.setBio("Experienced tutor");
        assertEquals("Experienced tutor", response.getBio());
    }

    @Test
    @DisplayName("Should set and get subject")
    void setAndGetSubject_WorksCorrectly() {
        response.setSubject("Mathematics");
        assertEquals("Mathematics", response.getSubject());
    }

    @Test
    @DisplayName("Should set and get hourly rate")
    void setAndGetHourlyRate_WorksCorrectly() {
        BigDecimal rate = BigDecimal.valueOf(50.00);
        response.setHourlyRate(rate);
        assertEquals(rate, response.getHourlyRate());
    }

    @Test
    @DisplayName("Should set and get years of experience")
    void setAndGetYearsOfExperience_WorksCorrectly() {
        response.setYearsOfExperience(5);
        assertEquals(5, response.getYearsOfExperience());
    }

    @Test
    @DisplayName("Should set and get average rating")
    void setAndGetAverageRating_WorksCorrectly() {
        response.setAverageRating(4.5);
        assertEquals(4.5, response.getAverageRating());
    }

    @Test
    @DisplayName("Should set and get total sessions")
    void setAndGetTotalSessions_WorksCorrectly() {
        response.setTotalSessions(10);
        assertEquals(10, response.getTotalSessions());
    }

    @Test
    @DisplayName("Should set and get status")
    void setAndGetStatus_WorksCorrectly() {
        response.setStatus(TutorProfile.TutorStatus.APPROVED);
        assertEquals(TutorProfile.TutorStatus.APPROVED, response.getStatus());
    }

    @Test
    @DisplayName("Should set and get created at")
    void setAndGetCreatedAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        response.setCreatedAt(now);
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updated at")
    void setAndGetUpdatedAt_WorksCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        response.setUpdatedAt(now);
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get user info")
    void setAndGetUser_WorksCorrectly() {
        TutorProfileResponse.UserInfo userInfo = new TutorProfileResponse.UserInfo();
        userInfo.setId(1L);
        userInfo.setEmail("tutor@example.com");
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("1234567890");
        userInfo.setRole("TUTOR");

        response.setUser(userInfo);

        assertNotNull(response.getUser());
        assertEquals("John", response.getUser().getFirstName());
        assertEquals("Doe", response.getUser().getLastName());
    }

    @Test
    @DisplayName("Should test UserInfo getters and setters")
    void userInfo_GettersAndSetters_WorkCorrectly() {
        TutorProfileResponse.UserInfo userInfo = new TutorProfileResponse.UserInfo();
        userInfo.setId(1L);
        userInfo.setEmail("test@example.com");
        userInfo.setFirstName("John");
        userInfo.setLastName("Doe");
        userInfo.setPhoneNumber("1234567890");
        userInfo.setRole("STUDENT");

        assertEquals(1L, userInfo.getId());
        assertEquals("test@example.com", userInfo.getEmail());
        assertEquals("John", userInfo.getFirstName());
        assertEquals("Doe", userInfo.getLastName());
        assertEquals("1234567890", userInfo.getPhoneNumber());
        assertEquals("STUDENT", userInfo.getRole());
    }
}
