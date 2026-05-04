package com.tutoring.service;

import com.tutoring.dto.RatingRequest;
import com.tutoring.model.Booking;
import com.tutoring.model.Rating;
import com.tutoring.model.TutorProfile;
import com.tutoring.model.User;
import com.tutoring.repository.BookingRepository;
import com.tutoring.repository.RatingRepository;
import com.tutoring.repository.TutorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Transactional
    public Rating createRating(Long studentId, RatingRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Not authorized to rate this session");
        }

        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed sessions");
        }

        if (booking.getRating() != null) {
            throw new RuntimeException("Session already rated");
        }

        Rating rating = new Rating();
        rating.setBooking(booking);
        rating.setStudent(booking.getStudent());
        rating.setTutor(booking.getSession().getTutor());
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());

        Rating savedRating = ratingRepository.save(rating);

        updateTutorAverageRating(booking.getSession().getTutor().getId());

        return savedRating;
    }

    public List<Rating> getTutorRatings(Long tutorId) {
        return ratingRepository.findByTutorId(tutorId);
    }

    public List<Rating> getStudentRatings(Long studentId) {
        return ratingRepository.findByStudentId(studentId);
    }

    private void updateTutorAverageRating(Long tutorId) {
        List<Rating> ratings = ratingRepository.findByTutorId(tutorId);

        double averageRating = ratings.stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);

        TutorProfile profile = tutorProfileRepository.findByUserId(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor profile not found"));

        profile.setAverageRating(averageRating);
        profile.setTotalSessions(ratings.size());

        tutorProfileRepository.save(profile);
    }
}