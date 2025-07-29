package com.eventhub.events.model;

import java.time.LocalDateTime;

public class Users {
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Users() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getUserId() { return this.userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return this.phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}