package com.eventhub.events.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eventhub.events.model.Ticket;
import com.eventhub.events.dao.TicketDao;
import com.eventhub.events.utils.QRCodegenerator;
import com.eventhub.events.utils.TicketNumberGen;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl implements TicketDao{
    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(TicketDaoImpl.class);

    public TicketDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        String insert_query = "INSERT INTO tickets(ticketNumber, eventDate, eventName, organizationName)" + "VALUES (?, ?, ?, ?)";
        String generatedTicketNumber = TicketNumberGen.generateTicketNumber();
        ticket.setTicketNumber(generatedTicketNumber);

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, ticket.getTicketNumber());
            stmt.setTimestamp(2, Timestamp.valueOf(ticket.getEventDate()));
            stmt.setString(3, ticket.getEventName());
            stmt.setString(4, ticket.getOrganizationName());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Created a ticket! User should provide the ticket at the venue");

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

                stmt.setString(1, ticket.getTicketNumber());
                stmt.setTimestamp(2, Timestamp.valueOf(ticket.getEventDate()));
                stmt.setString(3, ticket.getEventName());
                stmt.setString(4, ticket.getOrganizationName());
                stmt.setBytes(5, qrBytes);
            } else {
                logger.warn("Failed to create a ticket!");
            }
        } catch (SQLException e) {
            logger.error("Error while creating the ticket!", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ticket;
    }

    @Override
    public List<Ticket> getTickets(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String fetch_query = "SELECT * FROM tickets WHERE user_id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(fetch_query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred while trying to fetch tickets for user {}", userId, e);
        }
        return tickets;
    }

    private Ticket mapRow(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(rs.getString("ticketNumber"));
        ticket.setUsername(rs.getString("username"));
        ticket.setEventDate(rs.getTimestamp("eventDate").toLocalDateTime());
        ticket.setEventName(rs.getString("eventName"));
        ticket.setOrganizationName(rs.getString("organizationName"));
        ticket.setQrCode(rs.getBytes("qr_code"));
        return ticket;
    }
}
