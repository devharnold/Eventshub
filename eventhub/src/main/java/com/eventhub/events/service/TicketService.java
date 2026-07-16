package com.eventhub.events.service;

import com.eventhub.events.dao.TicketDao;
import com.eventhub.events.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketDao ticketDao;

    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public Ticket createTicket(Integer userId, Integer eventId) {

        Ticket ticket = new Ticket();

        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setStatus("VALID");

        return ticketDao.createTicket(ticket);
    }

    public List<Ticket> getTicketsForUser(Integer userId) {
        return ticketDao.getTickets(userId);
    }
}