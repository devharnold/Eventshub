package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.PaymentDao;
import com.eventhub.events.model.Payment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.*;

@Repository
public class PaymentDAOImpl implements PaymentDao {
    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(PaymentDAOImpl.class);

    public PaymentDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (transaction_id, amount, phone, reference, created_at) VALUES (?, ?, ?, ?, ?)";

        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getTransactionId());
            stmt.setString(2, payment.getAmount());
            stmt.setString(3, payment.getPhone());
            stmt.setString(4, payment.getPaymentRef());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getCreatedAt()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Payment saved: {}", payment.getTransactionId());
            } else {
                logger.warn("Failed to save payment: {}", payment.getTransactionId());
            }
        } catch (SQLException e) {
            logger.error("Error saving payment {}", payment.getTransactionId(), e);
        }
    }

    @Override
    public Payment findByPaymentRef(String transactionId) {
        String sql = "SELECT * FROM payments WHERE transaction_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transactionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching payment by transactionId {}", transactionId, e);
        }
        return null;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all payments", e);
        }

        return payments;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("paymentId"));  // id as String
        payment.setTransactionId(rs.getString("transactionId"));
        payment.setAmount(rs.getString("amount"));  // amount as String
        payment.setPhone(rs.getString("phone"));
        payment.setPaymentRef(rs.getString("paymentRef"));
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return payment;
    }
}
