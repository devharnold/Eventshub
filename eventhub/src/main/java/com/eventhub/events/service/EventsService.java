package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService {
    private final EventsDao eventsDao;

    @Autowired
    public EventsService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    public Events getEventById(String eventId) {
        return eventsDao.findById(eventId);
    }

    public Events getEventByName(String eventName) {
        return eventsDao.findByName(eventName);
    }

    public List<Events> getAllEvents() {
        return eventsDao.findAll();
    }

    public List<Events> getAllEventsByDateAndLocation(String eventDate, String Location, int  limit, int offset) {
        return eventsDao.findByDateAndLocation(eventDate, Location, limit, offset);
    }

    public void createEvent(Events events) {
        eventsDao.createEvent(events);
    }

    public boolean isEventFree(String eventId) {
        Events events = eventsDao.findById(eventId);
        if (events != null) {
            return Double.compare(events.getPrice(), 0.0) <= 0;
        }
        return false;
    }
}
