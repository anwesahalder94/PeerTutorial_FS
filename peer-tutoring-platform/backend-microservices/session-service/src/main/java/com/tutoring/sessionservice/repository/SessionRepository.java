package com.tutoring.sessionservice.repository;

import com.tutoring.sessionservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTutorId(Long tutorId);
    List<Session> findByStatus(Session.SessionStatus status);
    List<Session> findBySubjectContainingIgnoreCase(String subject);
    List<Session> findByStartTimeAfterAndStatus(LocalDateTime startTime, Session.SessionStatus status);
}
