package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.model.Events;
import com.eventhub.events.utils.UniqueIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class EventsDaoImpl implements EventsDao {

    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(EventsDaoImpl.class);

    public EventsDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Events createEvent(Events event) {
        String insert_query = "INSERT INTO events (eventId, eventName, eventOrganizer, location, eventDate, eventDuration, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String generatedId = UniqueIdGenerator.generateUniqueId();
        event.setEventId(generatedId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, event.getEventId());
            stmt.setString(2, event.getEventName());
            stmt.setString(3, event.getEventOrganizer());
            stmt.setString(4, event.getEventLocation()); // swapped with duration
            stmt.setDate(5, java.sql.Date.valueOf(event.getEventDate())); // LocalDate
            stmt.setString(6, event.getEventDuration());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Event created successfully with ID: " + generatedId);
            } else {
                logger.warn("Event insert returned 0 rows.");
            }

        } catch (SQLException e) {
            logger.error("Error while creating event", e);
        }
        return event;
    }


    @Override
    public Events findByName(String eventName) {
        String query = "SELECT * FROM events WHERE eventName = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while executing query", e);
        }
        return null;
    }

    @Override
    public List<Events> findByDateAndLocation(String eventDate, String Location, int limit, int offset) {
        List<Events> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE eventDate = ? AND eventLocation = ? ORDER BY eventDate LIMIT ? OFFSET ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(eventDate));
            stmt.setString(2, Location);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error while executing query", e);
        }
        //return events.isEmpty() ? null : events.get(0);
        return events;
    }

    @Override
    public List<Events> findAll() {
        String query = "SELECT * FROM events";
        List<Events> events = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error while executing query", e);
        }
        return events;
    }

    private Events mapRow(ResultSet rs) throws SQLException {
        return new Events();
    }
}