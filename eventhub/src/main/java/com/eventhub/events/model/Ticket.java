package com.eventhub.events.model;

import java.time.LocalDateTime;
import java.util.Base64;

public class Ticket {

    // Database fields
    private Integer id;
    private String ticketNumber;
    private Integer userId;
    private Integer eventId;
    private byte[] qrCode;
    private LocalDateTime issuedAt;
    private String status;

    // Display fields (loaded via SQL JOINs)
    private String username;
    private String eventName;
    private String organizationName;
    private LocalDateTime eventDate;

    public Ticket() {
    }

    public Ticket(
            Integer id,
            String ticketNumber,
            Integer userId,
            Integer eventId,
            byte[] qrCode,
            LocalDateTime issuedAt,
            String status
    ) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.userId = userId;
        this.eventId = eventId;
        this.qrCode = qrCode;
        this.issuedAt = issuedAt;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getQrCodeBase64() {
        return qrCode != null
                ? Base64.getEncoder().encodeToString(qrCode)
                : null;
    }
}