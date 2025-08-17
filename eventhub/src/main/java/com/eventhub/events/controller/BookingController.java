package com.eventhub.events.controller;

import com.eventhub.events.model.Booking;
import com.eventhub.events.model.BookingRequest;
import com.eventhub.events.dto.TicketDTO;
import com.eventhub.events.model.Ticket;
import com.eventhub.events.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> bookTicket(@RequestBody BookingRequest request) {
        try {
            Ticket ticket = bookingService.bookTicket(request);
            TicketDTO ticketDTO = new TicketDTO(
                    ticket.getTicketNumber(),
                    ticket.getUsername(),
                    ticket.getEventName(),
                    ticket.getOrganizationName(),
                    ticket.getEventDate()
            );
        } catch (PaymentFailedException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
