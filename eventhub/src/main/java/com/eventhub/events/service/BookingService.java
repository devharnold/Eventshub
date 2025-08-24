package com.eventhub.events.service;

import com.eventhub.events.model.Ticket;
import com.eventhub.events.dao.TicketDao;
import com.eventhub.events.model.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final TicketService ticketService;
    private final PaymentService paymentService;
    private final TicketDao ticketDao;
    private final EventsService eventsService;

    @Autowired
    public BookingService(TicketService ticketService, PaymentService paymentService, TicketDao ticketDao, EventsService eventsService) {
        this.ticketService = ticketService;
        this.paymentService = paymentService;
        this.ticketDao = ticketDao;
        this.eventsService = eventsService;
    }

    public Ticket bookTicket(BookingRequest request) throws Exception {
        boolean isFreeEvent = eventsService.isEventFree(request.getEventId());

        boolean paymentConfirmed = true;
        if (!isFreeEvent) {
            paymentConfirmed = paymentService.confirmPayment(request.getPaymentId());
        }

        if (paymentConfirmed) {
            return ticketService.createTicket(request.getUserId(), request.getEventId());
        }

        throw new PaymentFailedException(
                "Payment could not be confirmed for the event: " + request.getEventId()
        );
    }
}
