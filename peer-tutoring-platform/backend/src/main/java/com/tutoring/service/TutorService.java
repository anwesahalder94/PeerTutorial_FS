package com.tutoring.service;

import com.tutoring.dto.TutorProfileRequest;
import com.tutoring.model.TutorProfile;
import com.tutoring.model.User;
import com.tutoring.repository.TutorProfileRepository;
import com.tutoring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public TutorProfile createProfile(Long userId, TutorProfileRequest request) {
        // Check if profile already exists
        TutorProfile existingProfile = getProfileByUserId(userId);
        if (existingProfile != null) {
            throw new RuntimeException("Profile already exists. Use update instead.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TutorProfile profile = new TutorProfile();
        profile.setUser(user);
        profile.setBio(request.getBio());
        profile.setSubject(request.getSubject());
        profile.setSubjects(request.getSubjects());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setStatus(TutorProfile.TutorStatus.PENDING);
        profile.setAverageRating(0.0);
        profile.setTotalSessions(0);

        return tutorProfileRepository.save(profile);
    }

    public TutorProfile getProfileByUserId(Long userId) {
        return tutorProfileRepository.findByUserId(userId).orElse(null);
    }

    public List<TutorProfile> getAllApprovedTutors() {
        return tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED);
    }

    public TutorProfile updateProfile(Long userId, TutorProfileRequest request) {
        TutorProfile profile = getProfileByUserId(userId);
        if (profile == null) {
            throw new RuntimeException("Profile not found. Create a profile first.");
        }

        profile.setBio(request.getBio());
        profile.setSubject(request.getSubject());
        profile.setSubjects(request.getSubjects());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setYearsOfExperience(request.getYearsOfExperience());

        return tutorProfileRepository.save(profile);
    }

    public List<TutorProfile> searchTutors(String subject, Double minRating) {
        return tutorProfileRepository.searchTutors(subject, minRating);
    }
}