package com.eventhub.events.model;

public class BookingRequest {

    private Integer userId;
    private Integer eventId;
    private String paymentId;

    public BookingRequest() {
    }

    public BookingRequest(Integer userId, Integer eventId, String paymentId) {
        this.userId = userId;
        this.eventId = eventId;
        this.paymentId = paymentId;
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}