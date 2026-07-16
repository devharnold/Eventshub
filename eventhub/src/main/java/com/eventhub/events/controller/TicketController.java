package com.eventhub.events.controller;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.eventhub.events.dto.TicketDTO;
import com.eventhub.events.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/user/{userId}")
    public List<TicketDTO> getTickets(@PathVariable int userId) {
        return ticketService.getTicketsForUser(userId)
                .stream()
                .map(ticket -> {
                    String qrCodeBase64 = ticket.getQrCode() != null
                            ? Base64.getEncoder().encodeToString(ticket.getQrCode())
                            : null;
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