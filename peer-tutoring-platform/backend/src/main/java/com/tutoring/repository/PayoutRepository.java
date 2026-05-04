package com.tutoring.repository;

import com.tutoring.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {
    List<Payout> findByTutorId(Long tutorId);

    List<Payout> findByStatus(Payout.PayoutStatus status);
}