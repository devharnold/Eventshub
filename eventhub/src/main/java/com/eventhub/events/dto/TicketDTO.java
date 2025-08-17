package com.eventhub.events.dto;


import java.time.LocalDateTime;

public record TicketDTO(String ticketNumber, String username, LocalDateTime eventDate, String eventName, String organizationName, String qrCodeBase64) {}