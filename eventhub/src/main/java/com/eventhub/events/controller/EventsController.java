package com.eventhub.events.controller;

import java.util.List;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.model.Events;
import com.eventhub.events.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
    private final EventsService eventsService;

    @Autowired
    public EventsController(EventsDao eventsDao) {
        this.eventsService = new EventsService(eventsDao);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable String eventId) {
        Events events = eventsService.getEventById(eventId);
        return (events != null) ? ResponseEntity.ok(events) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Events>> getEvents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String eventDate) {
        if (location != null && eventDate != null) {
            return ResponseEntity.ok(eventsService.getAllEventsByDateAndLocation(location, eventDate));
        }
        return ResponseEntity.ok(eventsService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<Events> createEvent(@RequestBody Events events) {
        eventsService.createEvent(events);
        return ResponseEntity.ok(events);
    }
}
