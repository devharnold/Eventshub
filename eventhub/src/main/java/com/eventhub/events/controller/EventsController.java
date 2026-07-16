package com.eventhub.events.controller;

import com.eventhub.events.model.Events;
import com.eventhub.events.service.EventsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable Integer eventId) {
        Events event = eventsService.getEventById(eventId);

        return (event != null)
                ? ResponseEntity.ok(event)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Events>> getEvents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String eventDate) {

        if (location != null && eventDate != null) {
            return ResponseEntity.ok(
                    eventsService.getAllEventsByDateAndLocation(eventDate, location)
            );
        }

        return ResponseEntity.ok(eventsService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<Events> createEvent(@RequestBody Events event) {
        eventsService.createEvent(event);
        return ResponseEntity.ok(event);
    }
}