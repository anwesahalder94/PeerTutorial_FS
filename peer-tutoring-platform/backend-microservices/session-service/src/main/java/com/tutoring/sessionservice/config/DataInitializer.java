package com.tutoring.sessionservice.config;

import com.tutoring.sessionservice.model.Session;
import com.tutoring.sessionservice.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void run(String... args) {
        if (sessionRepository.count() == 0) {
            // Session 1: Introduction to Calculus (Tutor ID 2)
            Session session1 = new Session();
            session1.setTutorId(2L);
            session1.setTitle("Introduction to Calculus");
            session1.setDescription("Learn the fundamentals of calculus including limits, derivatives, and integrals.");
            session1.setSubject("Mathematics");
            session1.setStartTime(LocalDateTime.now().plusDays(2));
            session1.setEndTime(LocalDateTime.now().plusDays(2).plusMinutes(90));
            session1.setPrice(BigDecimal.valueOf(50.00));
            session1.setMaxStudents(1);
            session1.setEnrolledStudents(0);
            session1.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session1);

            // Session 2: Advanced Algebra Workshop (Tutor ID 2)
            Session session2 = new Session();
            session2.setTutorId(2L);
            session2.setTitle("Advanced Algebra Workshop");
            session2.setDescription("Deep dive into algebraic structures and equations.");
            session2.setSubject("Mathematics");
            session2.setStartTime(LocalDateTime.now().plusDays(3));
            session2.setEndTime(LocalDateTime.now().plusDays(3).plusMinutes(90));
            session2.setPrice(BigDecimal.valueOf(45.00));
            session2.setMaxStudents(5);
            session2.setEnrolledStudents(0);
            session2.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session2);

            // Session 3: Physics Mechanics Fundamentals (Tutor ID 4)
            Session session3 = new Session();
            session3.setTutorId(4L);
            session3.setTitle("Physics Mechanics Fundamentals");
            session3.setDescription("Understanding Newton's laws and basic mechanics.");
            session3.setSubject("Physics");
            session3.setStartTime(LocalDateTime.now().plusDays(4));
            session3.setEndTime(LocalDateTime.now().plusDays(4).plusMinutes(90));
            session3.setPrice(BigDecimal.valueOf(40.00));
            session3.setMaxStudents(1);
            session3.setEnrolledStudents(0);
            session3.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session3);

            // Session 4: Thermodynamics Basics (Tutor ID 4)
            Session session4 = new Session();
            session4.setTutorId(4L);
            session4.setTitle("Thermodynamics Basics");
            session4.setDescription("Introduction to heat, energy, and thermodynamic principles.");
            session4.setSubject("Physics");
            session4.setStartTime(LocalDateTime.now().plusDays(5));
            session4.setEndTime(LocalDateTime.now().plusDays(5).plusMinutes(90));
            session4.setPrice(BigDecimal.valueOf(45.00));
            session4.setMaxStudents(3);
            session4.setEnrolledStudents(0);
            session4.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session4);

            // Session 5: Java Programming for Beginners (Tutor ID 5)
            Session session5 = new Session();
            session5.setTutorId(5L);
            session5.setTitle("Java Programming for Beginners");
            session5.setDescription("Learn Java from scratch - variables, loops, and OOP concepts.");
            session5.setSubject("Computer Science");
            session5.setStartTime(LocalDateTime.now().plusDays(6));
            session5.setEndTime(LocalDateTime.now().plusDays(6).plusMinutes(90));
            session5.setPrice(BigDecimal.valueOf(55.00));
            session5.setMaxStudents(1);
            session5.setEnrolledStudents(0);
            session5.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session5);

            // Session 6: Python Data Structures (Tutor ID 5)
            Session session6 = new Session();
            session6.setTutorId(5L);
            session6.setTitle("Python Data Structures");
            session6.setDescription("Master Python lists, dictionaries, and algorithms.");
            session6.setSubject("Computer Science");
            session6.setStartTime(LocalDateTime.now().plusDays(7));
            session6.setEndTime(LocalDateTime.now().plusDays(7).plusMinutes(90));
            session6.setPrice(BigDecimal.valueOf(50.00));
            session6.setMaxStudents(4);
            session6.setEnrolledStudents(0);
            session6.setStatus(Session.SessionStatus.AVAILABLE);
            sessionRepository.save(session6);

            System.out.println("✅ Created 6 sample sessions");
        }
    }
}
