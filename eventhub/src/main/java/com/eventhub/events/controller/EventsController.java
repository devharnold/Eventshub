package com.eventhub.events.controller;

import java.util.List;

import com.eventhub.events.model.Events;
import com.eventhub.events.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eventhub.events.repository.EventsRepository;

@RestController
@RequestMapping("/api/events")
public class EventsController {
    private final EventsService eventsService;

    @Autowired
    public EventsController(EventsRepository eventsRepository) {
        this.eventsService = new EventsService(eventsRepository);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable String eventId) {
        Events events = eventsService.getEventById(eventId);
        return (events != null) ? ResponseEntity.ok(events) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents() {
        return ResponseEntity.ok(eventsService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<Events> createEvent(@RequestBody Events events) {
        eventsService.createEvent(events);
        return ResponseEntity.ok(events);
    }
}
