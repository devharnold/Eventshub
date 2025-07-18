package com.eventhub.events.model;

import java.time.LocalDateTime;

public class Events {
    private String eventId;
    private String eventName;
    private String eventOrganizer;
    private LocalDateTime eventDuration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public Events() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = this.eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventOrganizer() { return eventOrganizer; }
    public void setEventOrganizer(String eventOrganizer) { this.eventOrganizer = eventOrganizer; }

    public LocalDateTime getEventDuration() { return eventDuration; }
    public void setEventDuration(LocalDateTime eventDuration) { this.eventDuration = eventDuration; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
