package com.eventhub.events.service;

import java.time.LocalDateTime;
import java.util.List;

import com.eventhub.events.dto.TicketDTO;
import com.eventhub.events.model.Events;
import com.eventhub.events.model.Ticket;
import com.eventhub.events.dao.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketDao ticketDao;
    private final EventsService eventsService;

    @Autowired
    public TicketService(TicketDao ticketDao, EventsService eventsService) {
        this.ticketDao = ticketDao;
        this.eventsService = eventsService;
    }

    public Ticket createTicket(String userId, String eventId) {
        Events events = eventsService.getEventById(eventId);
        // Build the Ticket entity
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticket.getTicketNumber());
        ticket.setUsername(userId);
        ticket.setEventDate(events.getEventDate().atStartOfDay());
        ticket.setEventName(events.getEventName());
        ticket.setOrganizationName(events.getEventOrganizer());

        // Save using DAO (DAO will handle ticket number + QR code)
        return ticketDao.createTicket(ticket);
    }

    public List<Ticket> getTicketsForUser(int userId) {
        return ticketDao.getTickets(userId);
    }
}
