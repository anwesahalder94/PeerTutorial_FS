package com.tutoring.tutorservice.service;

import com.tutoring.tutorservice.client.SessionClient;
import com.tutoring.tutorservice.client.UserClient;
import com.tutoring.tutorservice.dto.*;
import com.tutoring.tutorservice.model.*;
import com.tutoring.tutorservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorProfileRepository tutorProfileRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PayoutRepository payoutRepository;

    @Mock
    private SessionClient sessionClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TutorService tutorService;

    private TutorProfile tutorProfile;
    private TutorProfileRequest profileRequest;
    private RatingRequest ratingRequest;
    private PayoutRequest payoutRequest;
    private SessionClient.SessionResponse sessionResponse;
    private UserClient.UserResponse userResponse;

    @BeforeEach
    void setUp() {
        tutorProfile = new TutorProfile();
        tutorProfile.setId(1L);
        tutorProfile.setUserId(1L);
        tutorProfile.setBio("Experienced math tutor");
        tutorProfile.setSubject("Mathematics");
        tutorProfile.setSubjects(Arrays.asList("Algebra", "Calculus"));
        tutorProfile.setHourlyRate(BigDecimal.valueOf(50.00));
        tutorProfile.setYearsOfExperience(5);
        tutorProfile.setStatus(TutorProfile.TutorStatus.PENDING);

        profileRequest = new TutorProfileRequest();
        profileRequest.setBio("Experienced math tutor");
        profileRequest.setSubject("Mathematics");
        profileRequest.setSubjects(Arrays.asList("Algebra", "Calculus"));
        profileRequest.setHourlyRate(BigDecimal.valueOf(50.00));
        profileRequest.setYearsOfExperience(5);

        ratingRequest = new RatingRequest();
        ratingRequest.setBookingId(1L);
        ratingRequest.setRating(5);
        ratingRequest.setComment("Great session!");

        payoutRequest = new PayoutRequest();
        payoutRequest.setTutorId(1L);
        payoutRequest.setAmount(BigDecimal.valueOf(100.00));
        payoutRequest.setPaymentMethod("Bank Transfer");
        payoutRequest.setNotes("Weekly payout");

        sessionResponse = new SessionClient.SessionResponse(
                1L, 1L, "Math Session", "Learn algebra", "Mathematics",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                BigDecimal.valueOf(50.00), 1, 0, "AVAILABLE", false, false
        );

        userResponse = new UserClient.UserResponse(
                1L, "tutor@example.com", "John", "Doe", "1234567890", "TUTOR", true
        );
    }

    @Test
    @DisplayName("Should create profile successfully")
    void createProfile_NewProfile_ReturnsSavedProfile() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(tutorProfileRepository.save(any(TutorProfile.class))).thenReturn(tutorProfile);

        // When
        TutorProfile result = tutorService.createProfile(1L, profileRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Mathematics", result.getSubject());
        assertEquals(TutorProfile.TutorStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when profile already exists")
    void createProfile_ExistingProfile_ThrowsException() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.of(tutorProfile));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tutorService.createProfile(1L, profileRequest));
        assertEquals("Profile already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should update profile successfully")
    void updateProfile_ExistingProfile_ReturnsUpdatedProfile() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.of(tutorProfile));
        when(tutorProfileRepository.save(any(TutorProfile.class))).thenReturn(tutorProfile);

        profileRequest.setBio("Updated bio");
        profileRequest.setSubject("Physics");

        // When
        TutorProfile result = tutorService.updateProfile(1L, profileRequest);

        // Then
        assertNotNull(result);
        verify(tutorProfileRepository).save(tutorProfile);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing profile")
    void updateProfile_NonExistingProfile_ThrowsException() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tutorService.updateProfile(1L, profileRequest));
        assertEquals("Profile not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get profile by user ID successfully")
    void getProfileByUserId_ExistingProfile_ReturnsProfile() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.of(tutorProfile));
        when(sessionClient.getTutorSessions(1L)).thenReturn(Arrays.asList(sessionResponse));

        // When
        TutorProfile result = tutorService.getProfileByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalSessions());
    }

    @Test
    @DisplayName("Should handle session client exception gracefully")
    void getProfileByUserId_SessionClientException_ReturnsProfile() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.of(tutorProfile));
        when(sessionClient.getTutorSessions(1L)).thenThrow(new RuntimeException("Service unavailable"));

        // When
        TutorProfile result = tutorService.getProfileByUserId(1L);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw exception when profile not found")
    void getProfileByUserId_NonExistingProfile_ThrowsException() {
        // Given
        when(tutorProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tutorService.getProfileByUserId(1L));
        assertEquals("Tutor profile not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get all approved profiles")
    void getAllApprovedProfiles_ReturnsApprovedProfiles() {
        // Given
        TutorProfile approvedProfile = new TutorProfile();
        approvedProfile.setStatus(TutorProfile.TutorStatus.APPROVED);
        when(tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED))
                .thenReturn(Arrays.asList(approvedProfile));

        // When
        List<TutorProfile> result = tutorService.getAllApprovedProfiles();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TutorProfile.TutorStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should get pending profiles")
    void getPendingProfiles_ReturnsPendingProfiles() {
        // Given
        when(tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.PENDING))
                .thenReturn(Arrays.asList(tutorProfile));

        // When
        List<TutorProfile> result = tutorService.getPendingProfiles();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TutorProfile.TutorStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should approve profile successfully")
    void approveProfile_PendingProfile_ApprovesProfile() {
        // Given
        when(tutorProfileRepository.findById(1L)).thenReturn(Optional.of(tutorProfile));
        when(tutorProfileRepository.save(any(TutorProfile.class))).thenReturn(tutorProfile);

        // When
        TutorProfile result = tutorService.approveProfile(1L);

        // Then
        assertNotNull(result);
        assertEquals(TutorProfile.TutorStatus.APPROVED, result.getStatus());
    }

    @Test
    @DisplayName("Should reject profile successfully")
    void rejectProfile_PendingProfile_RejectsProfile() {
        // Given
        when(tutorProfileRepository.findById(1L)).thenReturn(Optional.of(tutorProfile));
        when(tutorProfileRepository.save(any(TutorProfile.class))).thenReturn(tutorProfile);

        // When
        TutorProfile result = tutorService.rejectProfile(1L);

        // Then
        assertNotNull(result);
        assertEquals(TutorProfile.TutorStatus.REJECTED, result.getStatus());
    }

    @Test
    @DisplayName("Should create rating successfully")
    void createRating_ValidRequest_ReturnsRating() {
        // Given
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setBookingId(1L);
        rating.setTutorId(1L);
        rating.setStudentId(2L);
        rating.setRating(5);
        rating.setComment("Great session!");

        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        // When
        Rating result = tutorService.createRating(2L, ratingRequest);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Great session!", result.getComment());
    }

    @Test
    @DisplayName("Should get tutor ratings")
    void getTutorRatings_ExistingRatings_ReturnsRatings() {
        // Given
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setTutorId(1L);
        rating.setRating(5);
        when(ratingRepository.findByTutorId(1L)).thenReturn(Arrays.asList(rating));

        // When
        List<Rating> result = tutorService.getTutorRatings(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should create payout successfully")
    void createPayout_ValidRequest_ReturnsPayout() {
        // Given
        Payout payout = new Payout();
        payout.setId(1L);
        payout.setTutorId(1L);
        payout.setAmount(BigDecimal.valueOf(100.00));
        payout.setPaymentMethod("Bank Transfer");
        payout.setStatus(Payout.PayoutStatus.PENDING);

        when(payoutRepository.save(any(Payout.class))).thenReturn(payout);

        // When
        Payout result = tutorService.createPayout(payoutRequest, 1L);

        // Then
        assertNotNull(result);
        assertEquals(Payout.PayoutStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Should get all payouts")
    void getAllPayouts_ReturnsAllPayouts() {
        // Given
        Payout payout = new Payout();
        payout.setId(1L);
        when(payoutRepository.findAll()).thenReturn(Arrays.asList(payout));

        // When
        List<Payout> result = tutorService.getAllPayouts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get tutor payouts")
    void getTutorPayouts_ReturnsTutorPayouts() {
        // Given
        Payout payout = new Payout();
        payout.setId(1L);
        payout.setTutorId(1L);
        when(payoutRepository.findByTutorId(1L)).thenReturn(Arrays.asList(payout));

        // When
        List<Payout> result = tutorService.getTutorPayouts(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should process payout successfully")
    void processPayout_PendingPayout_CompletesPayout() {
        // Given
        Payout payout = new Payout();
        payout.setId(1L);
        payout.setTutorId(1L);
        payout.setAmount(BigDecimal.valueOf(100.00));
        payout.setStatus(Payout.PayoutStatus.PENDING);

        when(payoutRepository.findById(1L)).thenReturn(Optional.of(payout));
        when(payoutRepository.save(any(Payout.class))).thenReturn(payout);

        // When
        Payout result = tutorService.processPayout(1L, 2L);

        // Then
        assertNotNull(result);
        assertEquals(Payout.PayoutStatus.COMPLETED, result.getStatus());
        assertEquals(2L, result.getProcessedBy());
        assertNotNull(result.getProcessedAt());
    }

    @Test
    @DisplayName("Should throw exception when processing non-existing payout")
    void processPayout_NonExistingPayout_ThrowsException() {
        // Given
        when(payoutRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tutorService.processPayout(1L, 2L));
        assertEquals("Payout not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get approved profiles with user info")
    void getAllApprovedProfilesWithUser_ReturnsProfilesWithUserInfo() {
        // Given
        TutorProfile approvedProfile = new TutorProfile();
        approvedProfile.setId(1L);
        approvedProfile.setUserId(1L);
        approvedProfile.setBio("Math tutor");
        approvedProfile.setSubject("Mathematics");
        approvedProfile.setHourlyRate(BigDecimal.valueOf(50.00));
        approvedProfile.setStatus(TutorProfile.TutorStatus.APPROVED);

        when(tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED))
                .thenReturn(Arrays.asList(approvedProfile));
        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(sessionClient.getTutorSessions(1L)).thenReturn(Arrays.asList(sessionResponse));

        // When
        List<TutorProfileResponse> result = tutorService.getAllApprovedProfilesWithUser();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUser());
    }

    @Test
    @DisplayName("Should handle user client exception when getting profiles")
    void getAllApprovedProfilesWithUser_UserClientException_ReturnsProfilesWithoutUser() {
        // Given
        TutorProfile approvedProfile = new TutorProfile();
        approvedProfile.setId(1L);
        approvedProfile.setUserId(1L);
        approvedProfile.setStatus(TutorProfile.TutorStatus.APPROVED);

        when(tutorProfileRepository.findByStatus(TutorProfile.TutorStatus.APPROVED))
                .thenReturn(Arrays.asList(approvedProfile));
        when(userClient.getUserById(1L)).thenThrow(new RuntimeException("User service unavailable"));
        when(sessionClient.getTutorSessions(1L)).thenReturn(Arrays.asList());

        // When
        List<TutorProfileResponse> result = tutorService.getAllApprovedProfilesWithUser();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
