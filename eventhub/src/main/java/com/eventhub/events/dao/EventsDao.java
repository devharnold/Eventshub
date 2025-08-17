package com.eventhub.events.dao;

import java.sql.*;

import com.eventhub.events.model.Events;
import java.util.List;

public interface EventsDao {
    Events createEvent(Events event);
    Events findByName(String eventName);
    List<Events> findByDateAndLocation(String eventDate, String Location, int limit, int offset);
    List<Events> findAll();
    Events findById(String eventId);
}