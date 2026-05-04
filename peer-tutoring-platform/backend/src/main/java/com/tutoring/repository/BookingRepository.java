package com.tutoring.repository;

import com.tutoring.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStudentId(Long studentId);

    List<Booking> findBySessionTutorId(Long tutorId);

    List<Booking> findByStatus(Booking.BookingStatus status);
}