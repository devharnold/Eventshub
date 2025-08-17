package com.eventhub.events.dao;

import com.eventhub.events.model.Payment;
import java.util.List;

public interface PaymentDao {
    void save(Payment payment);
    Payment findByTransactionId(String transactionId);
    List<Payment> findAll();
}