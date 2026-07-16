package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.model.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventsDaoImpl implements EventsDao {

    private static final Logger logger =
            LoggerFactory.getLogger(EventsDaoImpl.class);

    private final DataSource dataSource;

    public EventsDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Events createEvent(Events event) {

        String sql = """
                INSERT INTO events
                (event_name,
                 organization_id,
                 location,
                 event_date,
                 event_duration,
                 price,
                 created_at,
                 updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventName());
            stmt.setInt(2, event.getOrganizationId());
            stmt.setString(3, event.getEventLocation());
            stmt.setDate(4, Date.valueOf(event.getEventDate()));
            stmt.setString(5, event.getEventDuration());
            stmt.setDouble(6, event.getPrice());
            stmt.setTimestamp(7, Timestamp.valueOf(event.getCreatedDate()));
            stmt.setTimestamp(8, Timestamp.valueOf(event.getUpdatedDate()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    event.setEventId(rs.getInt("id"));
                }
            }

            logger.info("Created event {}", event.getEventId());

        } catch (SQLException e) {
            logger.error("Failed to create event", e);
        }

        return event;
    }

    @Override
    public Events findById(Integer eventId) {

        String sql = "SELECT * FROM events WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding event {}", eventId, e);
        }

        return null;
    }

    @Override
    public Events findByName(String eventName) {

        String sql = "SELECT * FROM events WHERE event_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding event {}", eventName, e);
        }

        return null;
    }

    @Override
    public List<Events> findAll() {

        List<Events> events = new ArrayList<>();

        String sql = "SELECT * FROM events ORDER BY event_date";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Error retrieving events", e);
        }

        return events;
    }

    @Override
    public List<Events> findByDateAndLocation(
            String eventDate,
            String location,
            int limit,
            int offset) {

        List<Events> events = new ArrayList<>();

        String sql = """
                SELECT *
                FROM events
                WHERE event_date = ?
                AND location = ?
                ORDER BY event_date
                LIMIT ?
                OFFSET ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(eventDate));
            stmt.setString(2, location);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error retrieving events", e);
        }

        return events;
    }

    private Events mapRow(ResultSet rs) throws SQLException {

        Events event = new Events();

        event.setEventId(rs.getInt("id"));
        event.setEventName(rs.getString("event_name"));
        event.setOrganizationId(rs.getInt("organization_id"));
        event.setEventLocation(rs.getString("location"));
        event.setEventDate(rs.getDate("event_date").toLocalDate());
        event.setEventDuration(rs.getString("event_duration"));
        event.setPrice(rs.getDouble("price"));
        event.setCreatedDate(rs.getTimestamp("created_at").toLocalDateTime());
        event.setUpdatedDate(rs.getTimestamp("updated_at").toLocalDateTime());

        return event;
    }
}