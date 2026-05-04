package com.tutoring.service;

import com.tutoring.dto.PayoutRequest;
import com.tutoring.model.*;
import com.tutoring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private PayoutRepository payoutRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<TutorProfile> getPendingTutorProfiles() {
        return tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.PENDING);
    }

    @Transactional
    public TutorProfile approveTutor(Long profileId) {
        TutorProfile profile = tutorProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Tutor profile not found"));

        profile.setStatus(TutorProfile.TutorStatus.APPROVED);
        return tutorProfileRepository.save(profile);
    }

    @Transactional
    public TutorProfile rejectTutor(Long profileId) {
        TutorProfile profile = tutorProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Tutor profile not found"));

        profile.setStatus(TutorProfile.TutorStatus.REJECTED);
        return tutorProfileRepository.save(profile);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);
    }

    public Payout createPayout(Long adminId, PayoutRequest request) {
        User tutor = userRepository.findById(request.getTutorId())
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Payout payout = new Payout();
        payout.setTutor(tutor);
        payout.setAmount(request.getAmount());
        payout.setPaymentMethod(request.getPaymentMethod());
        payout.setNotes(request.getNotes());
        payout.setStatus(Payout.PayoutStatus.PENDING);
        payout.setProcessedBy(admin);

        return payoutRepository.save(payout);
    }

    public List<Payout> getAllPayouts() {
        return payoutRepository.findAll();
    }

    public List<Payout> getTutorPayouts(Long tutorId) {
        return payoutRepository.findByTutorId(tutorId);
    }

    @Transactional
    public Payout processPayout(Long payoutId, Long adminId) {
        Payout payout = payoutRepository.findById(payoutId)
                .orElseThrow(() -> new RuntimeException("Payout not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        payout.setStatus(Payout.PayoutStatus.COMPLETED);
        payout.setProcessedBy(admin);
        payout.setProcessedAt(LocalDateTime.now());

        return payoutRepository.save(payout);
    }

    public long getTotalBookings() {
        return bookingRepository.count();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalTutors() {
        return tutorProfileRepository.count();
    }
}
