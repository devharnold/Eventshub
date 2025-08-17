package com.eventhub.events.model;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String username;
    private String eventName;
    private String organizationName;
    private LocalDateTime eventDate;
    private double amount;
    private boolean paid;

    public Booking(String bookingId, String username, String eventName, String organizationName, LocalDateTime eventDate, double amount) {
        this.bookingId = bookingId;
        this.username = username;
        this.eventName = eventName;
        this.organizationName = organizationName;
        this.eventDate = eventDate;
        this.amount = amount;
        this.paid = false; // Default until payment confirmed
    }

    public String getBookingId() { return bookingId; }
    public String getUsername() { return username; }
    public String getEventName() { return eventName; }
    public String getOrganizationName() { return organizationName; }
    public LocalDateTime getEventDate() { return eventDate; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }

    public void markAsPaid() { this.paid = true; }
}
