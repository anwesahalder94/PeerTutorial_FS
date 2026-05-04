package com.tutoring.repository;

import com.tutoring.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTutorId(Long tutorId);

    List<Session> findByStatus(Session.SessionStatus status);

    @Query("SELECT s FROM Session s WHERE s.tutor.id = :tutorId AND " +
           "((s.startTime BETWEEN :startTime AND :endTime) OR (s.endTime BETWEEN :startTime AND :endTime))")
    List<Session> findOverlappingSessions(@Param("tutorId") Long tutorId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Session s WHERE s.subject LIKE %:subject% AND s.status = 'AVAILABLE' " +
           "AND s.startTime > :now")
    List<Session> findAvailableSessionsBySubject(@Param("subject") String subject, @Param("now") LocalDateTime now);
}