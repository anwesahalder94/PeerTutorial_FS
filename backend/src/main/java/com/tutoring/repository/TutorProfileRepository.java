package com.tutoring.repository;

import com.tutoring.model.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, Long> {
    Optional<TutorProfile> findByUserId(Long userId);

    List<TutorProfile> findByStatus(TutorProfile.TutorStatus status);

    @Query("SELECT tp FROM TutorProfile tp WHERE tp.status = 'APPROVED' AND " +
           "(:subject IS NULL OR :subject = '' OR tp.subject LIKE %:subject%) AND " +
           "(:minRating IS NULL OR tp.averageRating >= :minRating)")
    List<TutorProfile> searchTutors(@Param("subject") String subject, @Param("minRating") Double minRating);
}