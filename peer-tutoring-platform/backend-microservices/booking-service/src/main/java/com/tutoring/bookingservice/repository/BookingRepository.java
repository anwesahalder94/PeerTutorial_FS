package com.tutoring.bookingservice.repository;

import com.tutoring.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStudentId(Long studentId);
    List<Booking> findBySessionId(Long sessionId);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findBySessionIdIn(List<Long> sessionIds);
}
