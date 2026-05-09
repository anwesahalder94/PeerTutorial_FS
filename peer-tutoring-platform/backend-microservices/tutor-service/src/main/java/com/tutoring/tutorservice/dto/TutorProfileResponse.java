package com.tutoring.tutorservice.dto;

import com.tutoring.tutorservice.model.TutorProfile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TutorProfileResponse {
    private Long id;
    private Long userId;
    private String bio;
    private String subject;
    private List<String> subjects;
    private BigDecimal hourlyRate;
    private int yearsOfExperience;
    private double averageRating;
    private int totalSessions;
    private TutorProfile.TutorStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfo user;

    public static class UserInfo {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String role;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getTotalSessions() { return totalSessions; }
    public void setTotalSessions(int totalSessions) { this.totalSessions = totalSessions; }
    public TutorProfile.TutorStatus getStatus() { return status; }
    public void setStatus(TutorProfile.TutorStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
}
