package com.tutoring.tutorservice.repository;

import com.tutoring.tutorservice.model.TutorProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TutorProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Test
    @DisplayName("Should find profile by user ID")
    void findByUserId_ExistingProfile_ReturnsProfile() {
        // Given
        TutorProfile profile = new TutorProfile();
        profile.setUserId(1L);
        profile.setBio("Math tutor");
        profile.setSubject("Mathematics");
        profile.setHourlyRate(BigDecimal.valueOf(50.00));
        profile.setStatus(TutorProfile.TutorStatus.APPROVED);
        entityManager.persist(profile);
        entityManager.flush();

        // When
        Optional<TutorProfile> found = tutorProfileRepository.findByUserId(1L);

        // Then
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getUserId());
    }

    @Test
    @DisplayName("Should find profiles by status")
    void findByStatus_ApprovedStatus_ReturnsApprovedProfiles() {
        // Given
        TutorProfile approved = new TutorProfile();
        approved.setUserId(1L);
        approved.setBio("Approved tutor");
        approved.setSubject("Math");
        approved.setHourlyRate(BigDecimal.valueOf(50.00));
        approved.setStatus(TutorProfile.TutorStatus.APPROVED);

        TutorProfile pending = new TutorProfile();
        pending.setUserId(2L);
        pending.setBio("Pending tutor");
        pending.setSubject("Science");
        pending.setHourlyRate(BigDecimal.valueOf(60.00));
        pending.setStatus(TutorProfile.TutorStatus.PENDING);

        entityManager.persist(approved);
        entityManager.persist(pending);
        entityManager.flush();

        // When
        List<TutorProfile> found = tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED);

        // Then
        assertEquals(1, found.size());
        assertEquals(TutorProfile.TutorStatus.APPROVED, found.get(0).getStatus());
    }

    @Test
    @DisplayName("Should find profiles by subject containing")
    void findBySubjectContainingIgnoreCase_MatchingSubject_ReturnsProfiles() {
        // Given
        TutorProfile profile = new TutorProfile();
        profile.setUserId(1L);
        profile.setBio("Tutor");
        profile.setSubject("Mathematics");
        profile.setHourlyRate(BigDecimal.valueOf(50.00));
        profile.setStatus(TutorProfile.TutorStatus.APPROVED);
        entityManager.persist(profile);
        entityManager.flush();

        // When
        List<TutorProfile> found = tutorProfileRepository.findBySubjectContainingIgnoreCase("math");

        // Then
        assertFalse(found.isEmpty());
    }
}
