package com.eventhub.events.model;

public class BookingRequest {
    private String userId;
    private String eventId;
    private String paymentId;

    public BookingRequest(String userId, String eventId, String paymentId) {
        this.userId = userId;
        this.eventId = eventId;
        this.paymentId = paymentId;
    }

    public BookingRequest() {}

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}

