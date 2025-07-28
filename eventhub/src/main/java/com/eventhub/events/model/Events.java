package com.eventhub.events.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Events {
    private String eventId;
    private String eventName;
    private String eventOrganizer;
    private String Location;
    private LocalDate eventDate;
    private String eventDuration;
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

    public String getEventLocation() { return Location; }
    public void setEventLocation(String location) { this.Location = location; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate( LocalDate eventDate ) { this.eventDate = eventDate; }

    public String getEventDuration() { return eventDuration; }
    public void setEventDuration(String eventDuration) { this.eventDuration = eventDuration; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
