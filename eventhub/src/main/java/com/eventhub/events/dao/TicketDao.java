package com.eventhub.events.dao;

import java.util.List;
import com.eventhub.events.model.Ticket;

public interface TicketDao {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getTickets(int userId);
}