package com.eventhub.events.service;

import com.eventhub.events.model.BookingRequest;
import com.eventhub.events.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final TicketService ticketService;
    private final PaymentService paymentService;
    private final EventsService eventsService;

    @Autowired
    public BookingService(TicketService ticketService,
                          PaymentService paymentService,
                          EventsService eventsService) {
        this.ticketService = ticketService;
        this.paymentService = paymentService;
        this.eventsService = eventsService;
    }

    public Ticket bookTicket(BookingRequest request) throws Exception {

        boolean isFreeEvent = eventsService.isEventFree(request.getEventId());

        if (!isFreeEvent) {
            boolean paymentConfirmed =
                    paymentService.confirmPayment(request.getPaymentId());

            if (!paymentConfirmed) {
                throw new PaymentFailedException(
                        "Payment could not be confirmed for event ID: "
                                + request.getEventId()
                );
            }
        }

        // Free event or payment succeeded
        return ticketService.createTicket(
                request.getUserId(),
                request.getEventId()
        );
    }
}