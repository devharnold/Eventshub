package com.eventhub.events.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Events {
    private Integer eventId;
    private String eventName;
    private Integer organizationId;
    private String location;
    private LocalDate eventDate;
    private String eventDuration;
    private Double price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public Events() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public Integer getOrganizationId() { return organizationId; }
    public void setOrganizationId(Integer organizationId) { this.organizationId = organizationId; }

    public String getEventLocation() { return location; }
    public void setEventLocation(String location) { this.location = location; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate( LocalDate eventDate ) { this.eventDate = eventDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getEventDuration() { return eventDuration; }
    public void setEventDuration(String eventDuration) { this.eventDuration = eventDuration; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
