package com.eventhub.events.repository;

import java.sql.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

import com.eventhub.events.utils.UniqueIdGenerator;

import com.eventhub.events.model.Events;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EventsRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(EventsRepository.class);

    public EventsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Events findById(String eventId) {
        String sql = "SELECT * FROM events WHERE eventId = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching event with ID: {}", eventId, e);
        }
        return null;
    }

    public List<Events> findAll() {
        String sql = "SELECT * FROM events";
        List<Events> events = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.addRow(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all events!", e);
        }
        return events;
    }

    public Events createEvent(Events events) {
        String sql = "INSERT INTO events (eventName, eventOrganizer, eventDuration) VALUES (?, ?, ?) RETURNING eventId";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, events.getEventName());
            stmt.setString(2, events.getEventOrganizer());
            stmt.setString(3, events.getEventId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    //String.generatedId = rs.getString("eventId");
                    String generatedId = UniqueIdGenerator.generateUniqueId();
                    events.setEventId(generatedId);
                    logger.info("Created event with ID: {}", events.getEventId());
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating event with ID: {}", events, e);
        }
        return events;
    }

    private Events mapRow(ResultSet rs) throws SQLException {
        return new Events(
//                rs.getString("eventId"),
//                rs.getString("eventName"),
//                rs.getString("eventOrganizer"),
//                rs.getTimestamp("eventDuration").toLocalDateTime()
        );
    }

}
