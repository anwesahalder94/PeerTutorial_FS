package com.tutoring.dto;

import jakarta.validation.constraints.NotNull;

public class BookingRequest {
    @NotNull
    private Long sessionId;

    private String notes;

    public BookingRequest() {}

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
