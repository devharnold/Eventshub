package com.eventhub.events.service;

import com.eventhub.events.dao.EventsDao;
import com.eventhub.events.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class testEventsService {
    @Mock
    private EventsDao eventsDao;

    @InjectMocks
    private EventsService eventsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        Events mockEvent = new Events();
        mockEvent.setEventId("112");
        doNothing().when(eventsDao).createEvent(mockEvent);

        eventsService.createEvent(mockEvent);

        verify(eventsDao, times(1))
                .createEvent(mockEvent);
    }

    @Test
    void testGetEventById_Found() {
        Events mockEvent = new Events();
        mockEvent.setEventId("001");
        when(eventsDao.findById("001")).thenReturn(mockEvent);

        Events result = eventsService.getEventById("001");

        assertNotNull(result);
        assertEquals(mockEvent, result);
        assertEquals("001", result.getEventId());
        verify(eventsDao, times(1)).findById("001");
    }

    @Test
    void testGetEventByName() {
        Events mockEvent = new Events();
        mockEvent.setEventName("Tech Festival");
        when(eventsDao.findByName("Tech Festival")).thenReturn(mockEvent);

        Events result = eventsService.getEventByName("Tech Festival");

        assertNotNull(result);
        assertEquals("Tech Festival", result.getEventName());
        verify(eventsDao, times(1)).findByName("Tech Festival");

    }

    @Test
    void testGetAllEvents() {
        List<Events> mockEvents = Arrays.asList(new Events(), new Events());
        when(eventsDao.findAll()).thenReturn(mockEvents);

        List<Events> result = eventsService.getAllEvents();

        assertEquals(mockEvents, result);
        assertEquals(2, result.size());
        verify(eventsDao, times(1)).findAll();
    }

    @Test
    void testGetAllEventsByDateAndLocation() {
        List<Events> mockEvents = Arrays.asList(new Events());
        when(eventsDao.findByDateAndLocation(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(mockEvents);

        List<Events> result = eventsService.getAllEventsByDateAndLocation("2025-11-06", "Nairobi");

        assertEquals(1, result.size());
        verify(eventsDao, times(1))
                .findByDateAndLocation(eq("2025-11-06"), eq("Nairobi"), anyInt(), anyInt());
    }


    @Test
    void testIsEventFree_WhenPriceZero() {
        Events mockEvent = new Events();
        mockEvent.setEventId("123");
        mockEvent.setPrice(0.0);
        when(eventsDao.findById("123"))
                .thenReturn(mockEvent);

        boolean result = eventsService.isEventFree("123");

        assertTrue(result);
        verify(eventsDao, times(1))
                .findById("123");
    }

    @Test
    void testIsEventFree_WhenPricePositive() {
        Events mockEvent = new Events();
        mockEvent.setEventId("321");
        mockEvent.setPrice(50.0);
        when(eventsDao.findById("321"))
                .thenReturn(mockEvent);

        boolean result = eventsService.isEventFree("321");

        assertFalse(result);
        verify(eventsDao, times(1))
                .findById("321");
    }

    @Test
    void testIsEventFree_EventNotFound() {
        when(eventsDao.findById("404")).thenReturn(null);

        boolean result = eventsService.isEventFree("404");

        assertFalse(result);
        verify(eventsDao, times(1)).findById("404");
    }
}
