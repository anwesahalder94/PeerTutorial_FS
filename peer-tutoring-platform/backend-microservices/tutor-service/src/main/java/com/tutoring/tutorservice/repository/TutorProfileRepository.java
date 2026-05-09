package com.tutoring.tutorservice.repository;

import com.tutoring.tutorservice.model.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, Long> {
    Optional<TutorProfile> findByUserId(Long userId);
    List<TutorProfile> findByStatus(TutorProfile.TutorStatus status);
    List<TutorProfile> findBySubjectContainingIgnoreCase(String subject);
}
