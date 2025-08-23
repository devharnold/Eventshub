package com.eventhub.events.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.eventhub.events.dto.TicketDTO;
import com.eventhub.events.service.TicketService;

import com.eventhub.events.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets/user/{userId}")
    public List<TicketDTO> getTickets(@PathVariable int userId) {
        return ticketService.getTicketsForUser(userId)
                .stream()
                .map(ticket -> {
                    String qrCodeBase64 = null;
                    try {
                        if (ticket.getQrCode() != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(ticket.getQrCode(), "png", baos);
                            qrCodeBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Error encoding QR code for ticket: " + ticket.getTicketNumber());
                    }

                    return new TicketDTO(
                            ticket.getTicketNumber(),
                            ticket.getUsername(),
                            ticket.getEventDate(),
                            ticket.getEventName(),
                            ticket.getOrganizationName(),
                            qrCodeBase64
                    );
                })
                .collect(Collectors.toList());
    }

}
