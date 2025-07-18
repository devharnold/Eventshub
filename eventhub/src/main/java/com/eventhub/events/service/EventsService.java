package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.model.Events;
import com.eventhub.events.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService {
    private final EventsRepository eventsRepository;

    @Autowired
    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public Events getEventById(String eventId) {
        return eventsRepository.findById(eventId);
    }

    public List<Events> getAllEvents() {
        return eventsRepository.findAll();
    }

    public Events createEvent(Events events) {
        return eventsRepository.createEvent(events);
    }
}
