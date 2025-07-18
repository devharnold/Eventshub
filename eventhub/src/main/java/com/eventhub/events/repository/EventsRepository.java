package com.eventhub.events.repository;

import com.eventhub.events.model.Events;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class EventsRepository {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/database-name";
    private final String JDBC_USER = "postgres";
    private final String JDBC_PASSWORD = "postgres";

    public Events findById(String eventId) {
        String sql = "SELECT * FROM events WHERE eventId = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Events(
//                            rs.getString("eventId"),
//                            rs.getString("eventName"),
//                            rs.getString("eventOrganizer"),
//                            rs.getTimestamp("eventDuration").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logger in production
        }
        return null;
    }

    public List<Events> findAll() {
        List<Events> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(new Event(
//                        rs.getString("eventId"),
//                        rs.getString("eventName"),
//                        rs.getString("eventOrganizer"),
//                        rs.getTimestamp("eventDuration").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logger in production
        }
        return events;
    }

    public Events createEvent(Events event) {
        String sql = "INSERT INTO events (eventName, eventOrganizer, duration) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getEventOrganizer());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getEventDuration()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setEventId(generatedKeys.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logger in production
        }
        return event;
    }

}
