package com.eventhub.events.model;

import com.eventhub.events.utils.QRCodegenerator;

import java.awt.image.BufferedImage;

// TODO:

public class Ticket {
    private String username;
    private String eventName;
    private String organizationName;
    private String eventDate;
    private BufferedImage qrCode;

    public Ticket(String username, String eventName, String organizationName, String eventDate) throws Exception {
        this.username = username;
        this.eventName = eventName;
        this.organizationName = organizationName;
        this.eventDate = eventDate;
        this.qrCode = QRCodegenerator.generateTicketQRCode(username, eventName, organizationName, eventDate);
    }

    public String getUsername() {
        return username;
    }

    public String getEventName() {
        return eventName;
    }

    public String getOrganizationName() {
        return organizationName;
    }
    public String getEventDate() {
        return eventDate;
    }

}