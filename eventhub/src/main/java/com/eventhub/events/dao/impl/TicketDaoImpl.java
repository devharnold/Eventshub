package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.TicketDao;
import com.eventhub.events.model.Ticket;
import com.eventhub.events.utils.QRCodegenerator;
import com.eventhub.events.utils.TicketNumberGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDaoImpl implements TicketDao {

    private static final Logger logger =
            LoggerFactory.getLogger(TicketDaoImpl.class);

    private final DataSource dataSource;

    public TicketDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {

        String sql = """
                INSERT INTO tickets
                (
                    ticket_number,
                    user_id,
                    event_id,
                    qr_code,
                    issued_at,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        ticket.setTicketNumber(TicketNumberGen.generateTicketNumber());
        ticket.setIssuedAt(LocalDateTime.now());
        ticket.setStatus("ACTIVE");

        try {

            BufferedImage qrImage = QRCodegenerator.generateTicketQRCode(
                    ticket.getUsername(),
                    ticket.getEventName(),
                    ticket.getOrganizationName(),
                    ticket.getEventDate()
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);

            byte[] qrBytes = baos.toByteArray();
            ticket.setQrCode(qrBytes);

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, ticket.getTicketNumber());
                stmt.setInt(2, ticket.getUserId());
                stmt.setInt(3, ticket.getEventId());
                stmt.setBytes(4, qrBytes);
                stmt.setTimestamp(5, Timestamp.valueOf(ticket.getIssuedAt()));
                stmt.setString(6, ticket.getStatus());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ticket.setId(rs.getInt("id"));
                    }
                }

                logger.info("Created ticket {}", ticket.getTicketNumber());

            }

        } catch (SQLException e) {
            logger.error("Error creating ticket", e);
        } catch (IOException e) {
            throw new RuntimeException("Error generating QR code", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating ticket", e);
        }

        return ticket;
    }

    @Override
    public List<Ticket> getTickets(int userId) {

        List<Ticket> tickets = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.ticket_number,
                    t.user_id,
                    t.event_id,
                    t.qr_code,
                    t.issued_at,
                    t.status,
                    u.username,
                    e.event_name,
                    e.event_date,
                    o.organization_name
                FROM tickets t
                JOIN users u
                    ON t.user_id = u.id
                JOIN events e
                    ON t.event_id = e.id
                JOIN organizations o
                    ON e.organization_id = o.id
                WHERE t.user_id = ?
                ORDER BY t.issued_at DESC
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error retrieving tickets for user {}", userId, e);
        }

        return tickets;
    }

    private Ticket mapRow(ResultSet rs) throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setId(rs.getInt("id"));
        ticket.setTicketNumber(rs.getString("ticket_number"));
        ticket.setUserId(rs.getInt("user_id"));
        ticket.setEventId(rs.getInt("event_id"));
        ticket.setQrCode(rs.getBytes("qr_code"));
        ticket.setIssuedAt(rs.getTimestamp("issued_at").toLocalDateTime());
        ticket.setStatus(rs.getString("status"));

        // Display fields
        ticket.setUsername(rs.getString("username"));
        ticket.setEventName(rs.getString("event_name"));
        ticket.setEventDate(rs.getDate("event_date").toLocalDate().atStartOfDay());
        ticket.setOrganizationName(rs.getString("organization_name"));

        return ticket;
    }
}