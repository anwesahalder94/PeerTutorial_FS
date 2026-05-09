package com.tutoring.tutorservice.service;

import com.tutoring.tutorservice.client.SessionClient;
import com.tutoring.tutorservice.client.UserClient;
import com.tutoring.tutorservice.dto.*;
import com.tutoring.tutorservice.model.*;
import com.tutoring.tutorservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutorService {

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PayoutRepository payoutRepository;

    @Autowired
    private SessionClient sessionClient;

    @Autowired
    private UserClient userClient;

    public TutorProfile createProfile(Long userId, TutorProfileRequest request) {
        // Check if profile already exists
        if (tutorProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }
        TutorProfile profile = new TutorProfile();
        profile.setUserId(userId);
        profile.setBio(request.getBio());
        profile.setSubject(request.getSubject());
        profile.setSubjects(request.getSubjects());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        return tutorProfileRepository.save(profile);
    }

    public TutorProfile updateProfile(Long userId, TutorProfileRequest request) {
        TutorProfile profile = tutorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setBio(request.getBio());
        profile.setSubject(request.getSubject());
        profile.setSubjects(request.getSubjects());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        return tutorProfileRepository.save(profile);
    }

    public TutorProfile getProfileByUserId(Long userId) {
        TutorProfile profile = tutorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Tutor profile not found"));

        // Fetch actual session count from session-service
        try {
            List<SessionClient.SessionResponse> sessions = sessionClient.getTutorSessions(userId);
            profile.setTotalSessions(sessions.size());
        } catch (Exception e) {
            // If session service is unavailable, keep the stored value
            System.err.println("Failed to fetch session count: " + e.getMessage());
        }

        return profile;
    }

    public List<TutorProfile> getAllApprovedProfiles() {
        return tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED);
    }

    public List<TutorProfileResponse> getAllApprovedProfilesWithUser() {
        List<TutorProfile> profiles = tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED);
        return profiles.stream()
                .map(this::convertToResponseWithUser)
                .collect(Collectors.toList());
    }

    private TutorProfileResponse convertToResponseWithUser(TutorProfile profile) {
        TutorProfileResponse response = new TutorProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUserId());
        response.setBio(profile.getBio());
        response.setSubject(profile.getSubject());
        response.setSubjects(profile.getSubjects());
        response.setHourlyRate(profile.getHourlyRate());
        response.setYearsOfExperience(profile.getYearsOfExperience());
        response.setAverageRating(profile.getAverageRating());
        response.setTotalSessions(profile.getTotalSessions());
        response.setStatus(profile.getStatus());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());

        // Fetch user information
        try {
            UserClient.UserResponse user = userClient.getUserById(profile.getUserId());
            if (user != null) {
                TutorProfileResponse.UserInfo userInfo = new TutorProfileResponse.UserInfo();
                userInfo.setId(user.id());
                userInfo.setEmail(user.email());
                userInfo.setFirstName(user.firstName());
                userInfo.setLastName(user.lastName());
                userInfo.setPhoneNumber(user.phoneNumber());
                userInfo.setRole(user.role());
                response.setUser(userInfo);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch user info for tutor " + profile.getUserId() + ": " + e.getMessage());
        }

        // Fetch actual session count
        try {
            List<SessionClient.SessionResponse> sessions = sessionClient.getTutorSessions(profile.getUserId());
            response.setTotalSessions(sessions.size());
        } catch (Exception e) {
            System.err.println("Failed to fetch session count: " + e.getMessage());
        }

        return response;
    }

    public List<TutorProfile> getPendingProfiles() {
        return tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.PENDING);
    }

    @Transactional
    public TutorProfile approveProfile(Long profileId) {
        TutorProfile profile = tutorProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setStatus(TutorProfile.TutorStatus.APPROVED);
        return tutorProfileRepository.save(profile);
    }

    @Transactional
    public TutorProfile rejectProfile(Long profileId) {
        TutorProfile profile = tutorProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setStatus(TutorProfile.TutorStatus.REJECTED);
        return tutorProfileRepository.save(profile);
    }

    @Transactional
    public Rating createRating(Long studentId, RatingRequest request) {
        Rating rating = new Rating();
        rating.setBookingId(request.getBookingId());
        rating.setTutorId(request.getBookingId()); // This should be fetched from booking
        rating.setStudentId(studentId);
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        return ratingRepository.save(rating);
    }

    public List<Rating> getTutorRatings(Long tutorId) {
        return ratingRepository.findByTutorId(tutorId);
    }

    public Payout createPayout(PayoutRequest request, Long adminId) {
        Payout payout = new Payout();
        payout.setTutorId(request.getTutorId());
        payout.setAmount(request.getAmount());
        payout.setPaymentMethod(request.getPaymentMethod());
        payout.setNotes(request.getNotes());
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
        payout.setStatus(Payout.PayoutStatus.COMPLETED);
        payout.setProcessedBy(adminId);
        payout.setProcessedAt(LocalDateTime.now());
        return payoutRepository.save(payout);
    }
}
