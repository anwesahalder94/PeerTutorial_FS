package com.tutoring.sessionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String subject;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private BigDecimal price;

    private int maxStudents = 1;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
}
