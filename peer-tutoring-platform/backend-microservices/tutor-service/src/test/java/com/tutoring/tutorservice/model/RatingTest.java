package com.tutoring.tutorservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    private Rating rating;

    @BeforeEach
    void setUp() {
        rating = new Rating();
    }

    @Test
    @DisplayName("Should set and get ID")
    void setIdAndGetId_WorksCorrectly() {
        rating.setId(1L);
        assertEquals(1L, rating.getId());
    }

    @Test
    @DisplayName("Should set and get booking ID")
    void setBookingIdAndGetBookingId_WorksCorrectly() {
        rating.setBookingId(1L);
        assertEquals(1L, rating.getBookingId());
    }

    @Test
    @DisplayName("Should set and get tutor ID")
    void setTutorIdAndGetTutorId_WorksCorrectly() {
        rating.setTutorId(1L);
        assertEquals(1L, rating.getTutorId());
    }

    @Test
    @DisplayName("Should set and get student ID")
    void setStudentIdAndGetStudentId_WorksCorrectly() {
        rating.setStudentId(2L);
        assertEquals(2L, rating.getStudentId());
    }

    @Test
    @DisplayName("Should set and get rating value")
    void setRatingAndGetRating_WorksCorrectly() {
        rating.setRating(5);
        assertEquals(5, rating.getRating());
    }

    @Test
    @DisplayName("Should set and get comment")
    void setCommentAndGetComment_WorksCorrectly() {
        rating.setComment("Great session!");
        assertEquals("Great session!", rating.getComment());
    }

    @Test
    @DisplayName("Should get created at timestamp - initially null before persistence")
    void getCreatedAt_InitiallyNull() {
        // @CreationTimestamp only works with JPA persistence
        assertNull(rating.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle boundary rating values")
    void setRating_BoundaryValues_WorksCorrectly() {
        rating.setRating(1);
        assertEquals(1, rating.getRating());

        rating.setRating(5);
        assertEquals(5, rating.getRating());
    }
}
