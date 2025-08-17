package com.eventhub.events.model;

import com.eventhub.events.utils.QRCodegenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

// TODO:

public class Ticket {
    private String ticketNumber;
    private String username;
    private String eventName;
    private String organizationName;
    private LocalDateTime eventDate;
    private byte[] qrCode; // Store QR code as bytes for DB storage

    public Ticket() {
        // No-arg constructor for frameworks (JDBC, JSON mappers, etc.)
    }

    public Ticket(String ticketNumber, String username, String eventName, String organizationName, LocalDateTime eventDate) throws Exception {
        this.ticketNumber = ticketNumber;
        this.username = username;
        this.eventName = eventName;
        this.organizationName = organizationName;
        this.eventDate = eventDate;
        setQrCodeFromDetails(username, eventName, organizationName, eventDate);
    }

    // Generate QR code from ticket details and store as byte[]
    public void setQrCodeFromDetails(String username, String eventName, String organizationName, LocalDateTime eventDate) throws Exception {
        BufferedImage qrImage = QRCodegenerator.generateTicketQRCode(username, eventName, organizationName, eventDate);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        this.qrCode = baos.toByteArray();
    }

    // Getters and setters
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public RenderedImage getQrCode() { return qrCode; }
    public void setQrCode(byte[] qrCode) { this.qrCode = qrCode; }
}
