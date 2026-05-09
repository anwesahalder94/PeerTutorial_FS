package com.tutoring.tutorservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class TutorProfileRequest {
    @NotBlank
    private String bio;

    @NotBlank
    private String subject;

    private List<String> subjects;

    @NotNull
    private BigDecimal hourlyRate;

    private int yearsOfExperience;

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
}
