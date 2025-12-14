package com.eventhub.events.model;

import java.time.LocalDateTime;

public class Payment {

    private String paymentId;
    private String transactionId;
    private String amount;
    private String phone;
    private String paymentRef;
    private LocalDateTime createdAt;

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public String getPaymentId() { return paymentId;}
    public void setPaymentId(String paymentId) { this.paymentId = paymentId;}

    public String getTransactionId() { return transactionId;}
    public void setTransactionId(String transactionId) { this.transactionId = transactionId;}

    public String getAmount() { return amount;}
    public void setAmount(String amount) { this.amount = amount;}

    public String getPhone() { return phone;}
    public void setPhone(String phone) { this.phone = phone;}

    public String getPaymentRef() { return paymentRef;}
    public void setPaymentRef(String reference) { this.paymentRef = paymentRef;}

    public LocalDateTime getCreatedAt() { return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}
}
