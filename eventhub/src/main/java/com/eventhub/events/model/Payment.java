package com.eventhub.events.model;

import java.time.LocalDateTime;

public class Payment {

    private Integer paymentId;
    private String transactionId;
    private String amount;
    private String phone;
    private String reference;
    private LocalDateTime createdAt;

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public int getPaymentId() { return paymentId;}
    public void setPaymentId(int paymentId) { this.paymentId = paymentId;}

    public String getTransactionId() { return transactionId;}
    public void setTransactionId(String transactionId) { this.transactionId = transactionId;}

    public String getAmount() { return amount;}
    public void setAmount(String amount) { this.amount = amount;}

    public String getPhone() { return phone;}
    public void setPhone(String phone) { this.phone = phone;}

    public String getReference() { return reference;}
    public void setReference(String reference) { this.reference = reference;}

    public LocalDateTime getCreatedAt() { return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}
}
