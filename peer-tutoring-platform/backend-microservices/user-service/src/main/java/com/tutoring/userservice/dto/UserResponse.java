package com.tutoring.userservice.dto;

import com.tutoring.userservice.model.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String role;
    private boolean active;
    private boolean verified;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().name();
        this.active = user.isActive();
        this.verified = user.isVerified();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public boolean isVerified() { return verified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
